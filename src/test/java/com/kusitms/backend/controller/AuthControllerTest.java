package com.kusitms.backend.controller;

import static com.kusitms.backend.ApiDocumentUtils.getDocumentRequest;
import static com.kusitms.backend.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kusitms.backend.config.TokenProvider;
import com.kusitms.backend.dto.SignInRequest;
import com.kusitms.backend.dto.TokenDto;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.service.IAuthService;
import com.kusitms.backend.util.RedisClient;
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

    TokenDto response = TokenDto.builder()
        .accessToken("accessToken")
        .refreshToken("refreshToken")
        .build();

    given(authService.signIn(dto)).willReturn(response);

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
                    fieldWithPath("data").description("응답 데이터")
                )));
  }
}