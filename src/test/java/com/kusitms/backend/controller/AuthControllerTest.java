package com.kusitms.backend.controller;

import static com.kusitms.backend.ApiDocumentUtils.getDocumentRequest;
import static com.kusitms.backend.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kusitms.backend.config.JwtAccessDeniedHandler;
import com.kusitms.backend.config.JwtAuthenticationEntryPoint;
import com.kusitms.backend.config.JwtSecurityConfig;
import com.kusitms.backend.config.TokenProvider;
import com.kusitms.backend.dto.AuthDto;
import com.kusitms.backend.dto.CheckSmsRequest;
import com.kusitms.backend.dto.SignInRequest;
import com.kusitms.backend.dto.TokenDto;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.service.IAuthService;
import com.kusitms.backend.service.IUserService;
import com.kusitms.backend.util.RedisClient;
import com.kusitms.backend.util.SmsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AuthControllerTest {

  @Autowired
  MockMvc mockMvc;
  @MockBean
  IAuthService authService;
  @MockBean
  TokenProvider tokenProvider;
  @MockBean
  UserRepository userRepository;
  @MockBean
  RedisClient redisClient;
  @Autowired
  ObjectMapper objectMapper;
  @MockBean
  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  @MockBean
  JwtAccessDeniedHandler jwtAccessDeniedHandler;
  @MockBean
  JwtSecurityConfig jwtSecurityConfig;
  @MockBean
  SmsClient smsClient;
  @MockBean
  IUserService userService;
  @MockBean
  private Authentication authentication;
  @MockBean
  private SecurityContext securityContext;

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentationContextProvider) {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .addFilters(new CharacterEncodingFilter("UTF-8", true))
        .apply(documentationConfiguration(restDocumentationContextProvider)
            .operationPreprocessors()
            .withRequestDefaults(prettyPrint())
            .withResponseDefaults(prettyPrint()))
        .build();
  }

  @Test
  @DisplayName("로컬 로그인")
  void signIn() throws Exception {
    SignInRequest dto = SignInRequest.builder()
        .email("test")
        .password("test12345")
        .build();

    TokenDto tokenDto = TokenDto.builder()
        .grantType("bearer")
        .accessToken("access-token")
        .refreshToken("refresh-token")
        .accessTokenExpireTime(30L)
        .build();

    given(authService.signIn(dto)).willReturn(tokenDto);

    String data = objectMapper.writeValueAsString(dto);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .post("/api/auth/sign-in")
            .characterEncoding("utf-8")
            .content(data)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("sign-in", getDocumentRequest(), getDocumentResponse(),
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("비밀번호")
                ),
                responseFields(
                    fieldWithPath("status").description("결과 코드"),
                    fieldWithPath("message").description("응답 메세지"),
                    fieldWithPath("data.grantType").description("승인 유형"),
                    fieldWithPath("data.accessToken").description("AccessToken"),
                    fieldWithPath("data.refreshToken").description("RefreshToken"),
                    fieldWithPath("data.accessTokenExpireTime").description("AccessToken 만료시간")
                )));
  }

  @Test
  @DisplayName("회원가입")
  void signUp() throws Exception {
    AuthDto.Request request = AuthDto.Request.builder()
        .email("test@naver.com")
        .password("test12345")
        .nickname("test")
        .contact("010-0000-0000")
        .build();

    AuthDto.Response response = AuthDto.Response.builder()
        .nickname("test")
        .build();

    given(authService.signUp(request)).willReturn(response.getNickname());

    String data = objectMapper.writeValueAsString(request);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .post("/api/auth/sign-up")
            .characterEncoding("utf-8")
            .content(data)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    );

    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("sign-up", getDocumentRequest(), getDocumentResponse(),
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("nickname").description("닉네임"),
                    fieldWithPath("contact").description("전화번호")
                ),
                responseFields(
                    fieldWithPath("status").description("결과 코드"),
                    fieldWithPath("message").description("응답 메세지"),
                    fieldWithPath("data").description("닉네임")
                )));
  }

  @Test
  @DisplayName("전화번호 인증 코드 전송")
  void sendSmsCode() throws Exception {
    // request param
    String contact = "01012345678";

    userService.sendSmsCode(contact);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .post("/api/auth/send-sms?contact={contact}", contact)
            .characterEncoding("utf-8")
            .accept(MediaType.APPLICATION_JSON)
    );

    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("send-sms", getDocumentRequest(), getDocumentResponse(),
                requestParameters(
                    parameterWithName("contact").description("전화번호")
                ),
                responseFields(
                    fieldWithPath("status").description("결과 코드"),
                    fieldWithPath("message").description("응답 메세지"),
                    fieldWithPath("data").description("응답 데이터 없음")
                )
            )
        );
  }

  @Test
  @DisplayName("전화번호 인증코드 검증")
  void checkSmsCode() throws Exception {
    // request body
    CheckSmsRequest request = CheckSmsRequest
        .builder()
        .code("1234")
        .contact("01012345678")
        .build();

    String email = "test@test.com";

    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(email);

    userService.checkSmsCode(request, email);

    String json = objectMapper.writeValueAsString(request);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .post("/api/auth/check-sms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .characterEncoding("utf-8")
            .accept(MediaType.APPLICATION_JSON)
    );

    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("check-sms", getDocumentRequest(), getDocumentResponse(),
                requestFields(
                    fieldWithPath("code").description("인증코드"),
                    fieldWithPath("contact").description("전화번호")
                ),
                responseFields(
                    fieldWithPath("status").description("결과 코드"),
                    fieldWithPath("message").description("응답 메세지"),
                    fieldWithPath("data").description("응답 데이터 없음")
                )
            )
        );
  }
}
