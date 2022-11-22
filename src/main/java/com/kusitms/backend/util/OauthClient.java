package com.kusitms.backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kusitms.backend.domain.Authority;
import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.AuthDto;
import com.kusitms.backend.dto.SignInRequest;
import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import com.kusitms.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class OauthClient {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${custom.oauth2.kakao.client-id}")
  private String clientId;

  @Value("${custom.oauth2.kakao.client-secret}")
  private String clientSecret;

  @Value("${custom.oauth2.kakao.redirect-uri}")
  private String redirectUri;

  @Value("${custom.oauth2.kakao.token-uri}")
  private String tokenUri;

  @Value("${custom.oauth2.kakao.profile-uri}")
  private String profileUri;

  /**
   * KaKao 소셜 로그인 시, access-token 조회.
   *
   * @param code 프론트에서 넘어오는 code
   * @return accessToken 카카오 리소스 서버에서 돌려받은 토큰
   */
  public String getAccessToken(String code) {
    // (1) Header 생성
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type",
        "application/x-www-form-urlencoded;charset=utf-8");

    // (2) Param 생성
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.add("grant_type", "authorization_code");
    param.add("client_id", clientId);
    param.add("redirect_uri", redirectUri);
    param.add("code", code);

    // (3) 요청 보내기
    HttpEntity<MultiValueMap<String, String>> tokenReq = new HttpEntity<>(param, headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        tokenUri,
        HttpMethod.POST,
        tokenReq,
        String.class
    );

    // (4) 응답 파싱
    String responseBody = responseEntity.getBody();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = null;
    try {
      jsonNode = objectMapper.readTree(responseBody);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return jsonNode.get("access_token").asText();
  }

  /**
   * KaKao 소셜 로그인 시, 사용자 정보 조회.
   *
   * @param accessToken 카카오 서버에서 받은 토큰
   * @return AuthDto.Request 카카오 서버에서 받은 사용자 정보를 담은 DTO
   */
  public AuthDto.Request getProfile(String accessToken) throws JsonProcessingException {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);
    headers.add("Content-type",
        "application/x-www-form-urlencoded;charset=utf-8");

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<String> response = restTemplate.exchange(
        profileUri,
        HttpMethod.GET,
        request,
        String.class
    );

    String responseBody = response.getBody();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(responseBody);

    Long id = jsonNode.get("id").asLong();
    String email = jsonNode.get("kakao_account").get("email").asText();
    String nickname = jsonNode.get("properties").get("nickname").asText();

    AuthDto.Request authDto = AuthDto.Request.builder().build();
    authDto.setNickname(nickname);
    authDto.setEmail(email);

    return authDto;
  }

  /**
   * KaKao 소셜 로그인 시, 회원가입.
   *
   * @param authDto 사용자 정보를 담은 Request Dto
   * @return SignInRequest 회원가입 처리된 사용자 정보
   */
  public SignInRequest signUp(AuthDto.Request authDto) {

    // (1) 회원가입 처리를 위한 데이터
    SignInRequest signInRequest = SignInRequest
        .builder()
        .email(authDto.getEmail())
        .password(authDto.getEmail())
        .isInit(true)
        .build();

    if (userRepository.findByEmail(authDto.getEmail()).isEmpty()) {
      userRepository.save(User.builder()
          .email(authDto.getEmail())
          .password(passwordEncoder.encode(authDto.getEmail()))
          .nickname(authDto.getNickname())
          .authority(Authority.ROLE_USER)
          .build());
    }

    return signInRequest;
  }
}
