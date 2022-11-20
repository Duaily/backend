package com.kusitms.backend.controller;

import static com.kusitms.backend.ApiDocumentUtils.getDocumentRequest;
import static com.kusitms.backend.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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
import com.kusitms.backend.dto.UserDto;
import com.kusitms.backend.repository.DealRepository;
import com.kusitms.backend.repository.HouseRepository;
import com.kusitms.backend.repository.PostRepository;
import com.kusitms.backend.repository.RegionRepository;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.service.IHouseService;
import com.kusitms.backend.service.IUserService;
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
@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;
  @MockBean
  IUserService userService;
  @MockBean
  UserRepository userRepository;
  @MockBean
  TokenProvider tokenProvider;
  @Autowired
  ObjectMapper objectMapper;
  @MockBean
  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  @MockBean
  JwtAccessDeniedHandler jwtAccessDeniedHandler;
  @MockBean
  JwtSecurityConfig jwtSecurityConfig;
  @MockBean
  private Authentication authentication;
  @MockBean
  private SecurityContext securityContext;

  final String email = "test@test.com";

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
  @DisplayName("유저 정보 조회")
  void getUserInfo() throws Exception {
    final UserDto response = UserDto.builder()
        .email("test@naver.com")
        .nickname("test")
        .contact("010-0000-0000")
        .build();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(email);

    given(userService.getUserInfo(email)).willReturn(response);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .get("/api/user")
    );

    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("user-info", getDocumentRequest(), getDocumentResponse(),
                responseFields(
                    fieldWithPath("status").description("결과 코드"),
                    fieldWithPath("message").description("응답 메세지"),
                    fieldWithPath("data.email").description("이메일"),
                    fieldWithPath("data.nickname").description("닉네임"),
                    fieldWithPath("data.contact").description("전화번호")
                ))
        );



  }

}