package com.kusitms.backend.controller;

import static com.kusitms.backend.ApiDocumentUtils.getDocumentRequest;
import static com.kusitms.backend.ApiDocumentUtils.getDocumentResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kusitms.backend.config.JwtAccessDeniedHandler;
import com.kusitms.backend.config.JwtAuthenticationEntryPoint;
import com.kusitms.backend.config.JwtSecurityConfig;
import com.kusitms.backend.config.TokenProvider;
import com.kusitms.backend.dto.RegionDto;
import com.kusitms.backend.repository.RegionRepository;
import com.kusitms.backend.service.IRegionService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
@WebMvcTest(RegionController.class)
@MockBean(JpaMetamodelMappingContext.class)
class RegionControllerTest {

  @Autowired
  MockMvc mockMvc;
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
  @MockBean
  IRegionService regionService;
  @MockBean
  RegionRepository regionRepository;

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
  void create() throws Exception {
    // request body
    RegionDto request = RegionDto
        .builder()
        .title("속초의 자랑을 해볼게요")
        .content("속초는 강원도에서 유명한 관광지입니다. 바다와 산을 모두 즐길 수 있는 속초로 놀러오세요.")
        .name("속초")
        .reason("속초의 자연을 느낄 수 있고, 관광지와 다양한 먹거리가 있는 곳")
        .origin("속초의 유래는 .... ")
        .info("속초에는 설악산이 있습니다. 설악산의 케이블카는 .... ")
        .city("강원도")
        .street("속초시")
        .zipcode("12345")
        .imageUrls(List.of(
            "image1.address",
            "image2.address",
            "image3.address",
            "image4.address",
            "image5.address"
        ))
        .build();

    Long response = 1l;

    given(regionService.create(request)).willReturn(response);

    String json = objectMapper.writeValueAsString(request);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .post("/api/region")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .characterEncoding("utf-8")
            .accept(MediaType.APPLICATION_JSON)
    );

    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("create", getDocumentRequest(), getDocumentResponse(),
                requestFields(
                    fieldWithPath("title").description("지역 게시글 제목"),
                    fieldWithPath("content").description("지역 게시글 내용"),
                    fieldWithPath("name").description("지역명"),
                    fieldWithPath("reason").description("지역 간단 추천 이유 ( 1000자 이내 )"),
                    fieldWithPath("origin").description("지역 유래"),
                    fieldWithPath("info").description("지역 정보"),
                    fieldWithPath("imageUrls").description("지역 게시글 첨부 사진 주소 리스트 ( 최대 5장 )"),
                    fieldWithPath("city").description("도/시"),
                    fieldWithPath("street").description("도로명주소"),
                    fieldWithPath("zipcode").description("우편번호")
                ),
                responseFields(
                    fieldWithPath("status").description("결과 코드"),
                    fieldWithPath("message").description("응답 메세지"),
                    fieldWithPath("data").description("지역 게시글 ID")
                )
            )
        );
  }
}