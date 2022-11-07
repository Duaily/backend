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



  @Transactional
  public TokenDto signIn(SignInRequest request) {

    UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(
        request.getEmail(), request.getPassword()); // id/pw 기반으로 authenticationToken 생성

    Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

    TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

    redisClient.setValue(authentication.getName(), tokenDto.getRefreshToken(),
        tokenProvider.getRefreshTokenExpireTime());

    return tokenDto;
  }

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

    if (!tokenProvider.validateToken(request.getRefreshToken())) {
      throw new ApiException(ApiExceptionEnum.TOKEN_DISCREPANCY_EXCEPTION);
    }

    Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

    String refreshToken = Optional.ofNullable(
            redisClient.getValue(authentication.getName()))
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.TOKEN_EXPIRE_TIME_OUT_EXCEPTION));

    if (!refreshToken.equals(request.getRefreshToken())) {
      throw new ApiException(ApiExceptionEnum.TOKEN_DISCREPANCY_EXCEPTION);
    }

    TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

    redisClient.setValue(authentication.getName(), tokenDto.getRefreshToken(),
        tokenProvider.getRefreshTokenExpireTime());

    return tokenDto;
  }

  @Transactional
  public TokenDto kakaoSignIn(String code) throws JsonProcessingException {
    // (1) 인가코드로 AccessToken 요청
    String accessToken = oauthClient.getAccessToken(code);
    log.debug("access token form kakao : " + accessToken);
    // (2) 토큰으로 카카오 API 호출 - 유저 정보 조회
    AuthDto.Request authDto = oauthClient.getProfile(accessToken);
    log.debug("authDto from kakao : " + authDto.getEmail());
    // (3) 카카오 계정으로 회원가입 처리
    User user = oauthClient.signUp(authDto);
    log.debug("user id : " + user.getId());
    // (4) 로그인 처리
    UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(
        user.getEmail(), ""); // email 기반으로 authenticationToken 생성
    Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

    // (5) 토큰 DTO 생성
    TokenDto tokenDto =  tokenProvider.generateTokenDto(authentication);

    // (6) refresh token 레디스 저장
    redisClient.setValue(authentication.getName(), tokenDto.getRefreshToken(),
        tokenProvider.getRefreshTokenExpireTime());
    log.debug("token : " + tokenDto.getAccessToken());
    return tokenDto;
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

