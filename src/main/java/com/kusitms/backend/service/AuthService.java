package com.kusitms.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kusitms.backend.config.TokenProvider;
import com.kusitms.backend.domain.Authority;
import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.AuthDto;
import com.kusitms.backend.dto.SignInRequest;
import com.kusitms.backend.dto.TokenDto;
import com.kusitms.backend.dto.TokenRequestDto;
import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.util.OauthClient;
import com.kusitms.backend.util.RedisClient;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService {

  private final AuthenticationManagerBuilder managerBuilder;
  private final TokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final RedisClient redisClient;
  private final UserRepository userRepository;
  private final OauthClient oauthClient;


  /**
   * 소셜 로그인 .
   *
   * @param request 소셜 로그인 시, 사용자 이메일 정보를 담고 있다.
   * @return TokenDto access-token, refresh-token, expire-time, init
   * */
  @Transactional
  public TokenDto signIn(SignInRequest request) {

    UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(
        request.getEmail(), request.getPassword()); // id/pw 기반으로 authenticationToken 생성

    Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

    TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

    // 전화번호 등록 여부 확인
    String email = authentication.getName();
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.USER_NOT_FOUND_EXCEPTION));

    tokenDto.setInit(user.getComments() == null); // 전화번호 없다 -> true

    redisClient.setValue(authentication.getName(), tokenDto.getRefreshToken(),
        tokenProvider.getRefreshTokenExpireTime());

    return tokenDto;
  }

  /**
   * 비밀번호 확인.
   *
   * @param request 비밀번호 확인용
   * @param origin 비밀번호 원본
   *
   * */
  private void checkPassword(String request, String origin) {

    if (!passwordEncoder.matches(request, origin)) {
      throw new ApiException(ApiExceptionEnum.PASSWORD_DISCREPANCY_EXCEPTION);
    }
  }

  //회원가입
  @Transactional
  public String signUp(AuthDto.Request request) {

    User user = User.builder()
        .nickname(request.getNickname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .contact(request.getContact())
        .authority(Authority.ROLE_USER)
        .build();

    userRepository.save(user);

    return user.getNickname();
  }

  @Transactional
  public TokenDto reissue(TokenRequestDto request) {
    // (1) Refresh Token 유효성 검사
    if (!tokenProvider.validateToken(request.getRefreshToken())) {
      throw new ApiException(ApiExceptionEnum.REFRESHTOKEN_DISCREPANCY_EXCEPTION);
    }

    // (2) Refresh Token 으로부터 사용자 정보 조회
    Authentication authentication = tokenProvider.getAuthentication(request.getRefreshToken());

    // (3) Redis 에 저장된 Refresh Token 조회
    String refreshFromRedis = Optional.ofNullable(
            redisClient.getValue(authentication.getName()))
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.TOKEN_EXPIRE_TIME_OUT_EXCEPTION));

    // (4) Refresh Token 비교
    if (!refreshFromRedis.equals(request.getRefreshToken())) {
      throw new ApiException(ApiExceptionEnum.REFRESHTOKEN_DISCREPANCY_EXCEPTION);
    }

    // (5) Access Token & Refresh Token 재발급
    TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

    // (6) Refresh Token 레디스에 저장
    redisClient.setValue(authentication.getName(), tokenDto.getRefreshToken(),
        tokenProvider.getRefreshTokenExpireTime());

    return tokenDto;
  }

  @Transactional
  public SignInRequest kakaoSignIn(String code) throws JsonProcessingException {
    // (1) 인가코드로 AccessToken 요청
    String accessToken = oauthClient.getAccessToken(code);

    // (2) 토큰으로 카카오 API 호출 - 유저 정보 조회
    AuthDto.Request authDto = oauthClient.getProfile(accessToken);

    // (3) 카카오 계정으로 회원가입 처리
    // (4) 로그인 처리
    return oauthClient.signUp(authDto);
  }

  //닉네임 중복 체크
  @Transactional
  public boolean checkNicknameDuplication(String nickname) {
    return userRepository.existsByNickname(nickname);
  }

  //회원 탈퇴
  @Transactional
  public void withdrawal(Long userId) {
    Optional<User> user = userRepository.findById(userId);

    userRepository.delete(user.get()); // 삭제 아니고 빈 값으로 대체하기

    //없을 경우는?

  }

}

