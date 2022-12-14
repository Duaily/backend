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
   * ?????? ????????? .
   *
   * @param request ?????? ????????? ???, ????????? ????????? ????????? ?????? ??????.
   * @return TokenDto access-token, refresh-token, expire-time, init
   * */
  @Transactional
  public TokenDto signIn(SignInRequest request) {

    UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(
        request.getEmail(), request.getPassword()); // id/pw ???????????? authenticationToken ??????

    Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

    TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

    // ???????????? ?????? ?????? ??????
    String email = authentication.getName();
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.USER_NOT_FOUND_EXCEPTION));

    tokenDto.setInit(user.getComments() == null || user.getContact().equals("")); // ???????????? ?????? -> true

    redisClient.setValue(authentication.getName(), tokenDto.getRefreshToken(),
        tokenProvider.getRefreshTokenExpireTime());

    return tokenDto;
  }

  /**
   * ???????????? ??????.
   *
   * @param request ???????????? ?????????
   * @param origin ???????????? ??????
   *
   * */
  private void checkPassword(String request, String origin) {

    if (!passwordEncoder.matches(request, origin)) {
      throw new ApiException(ApiExceptionEnum.PASSWORD_DISCREPANCY_EXCEPTION);
    }
  }

  //????????????
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
    // (1) Refresh Token ????????? ??????
    if (!tokenProvider.validateToken(request.getRefreshToken())) {
      throw new ApiException(ApiExceptionEnum.REFRESHTOKEN_DISCREPANCY_EXCEPTION);
    }

    // (2) Refresh Token ???????????? ????????? ?????? ??????
    Authentication authentication = tokenProvider.getAuthentication(request.getRefreshToken());

    // (3) Redis ??? ????????? Refresh Token ??????
    String refreshFromRedis = Optional.ofNullable(
            redisClient.getValue(authentication.getName()))
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.TOKEN_EXPIRE_TIME_OUT_EXCEPTION));

    // (4) Refresh Token ??????
    if (!refreshFromRedis.equals(request.getRefreshToken())) {
      throw new ApiException(ApiExceptionEnum.REFRESHTOKEN_DISCREPANCY_EXCEPTION);
    }

    // (5) Access Token & Refresh Token ?????????
    TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

    // (6) Refresh Token ???????????? ??????
    redisClient.setValue(authentication.getName(), tokenDto.getRefreshToken(),
        tokenProvider.getRefreshTokenExpireTime());

    return tokenDto;
  }

  @Transactional
  public SignInRequest kakaoSignIn(String code) throws JsonProcessingException {
    // (1) ??????????????? AccessToken ??????
    String accessToken = oauthClient.getAccessToken(code);

    // (2) ???????????? ????????? API ?????? - ?????? ?????? ??????
    AuthDto.Request authDto = oauthClient.getProfile(accessToken);

    // (3) ????????? ???????????? ???????????? ??????
    // (4) ????????? ??????
    return oauthClient.signUp(authDto);
  }

  //????????? ?????? ??????
  @Transactional
  public boolean checkNicknameDuplication(String nickname) {
    return userRepository.existsByNickname(nickname);
  }

  //?????? ??????
  @Transactional
  public void withdrawal(Long userId) {
    Optional<User> user = userRepository.findById(userId);

    userRepository.delete(user.get()); // ?????? ????????? ??? ????????? ????????????

    //?????? ??????????

  }

}

