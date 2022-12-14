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
  IRegionService regionService;
  @MockBean
  RegionRepository regionRepository;
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
  void create() throws Exception {
    // request body
    RegionDto request = RegionDto
        .builder()
        .title("????????? ????????? ????????????")
        .content("????????? ??????????????? ????????? ??????????????????. ????????? ?????? ?????? ?????? ??? ?????? ????????? ???????????????.")
        .name("??????")
        .reason("????????? ????????? ?????? ??? ??????, ???????????? ????????? ???????????? ?????? ???")
        .origin("????????? ????????? .... ")
        .info("???????????? ???????????? ????????????. ???????????? ??????????????? .... ")
        .city("?????????")
        .street("?????????")
        .zipcode("12345")
        .imageUrls(List.of(
            "image1.address",
            "image2.address",
            "image3.address",
            "image4.address",
            "image5.address"
        ))
        .build();

    final Long response = 1L;

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
                    fieldWithPath("title").description("?????? ????????? ??????"),
                    fieldWithPath("content").description("?????? ????????? ??????"),
                    fieldWithPath("name").description("?????????"),
                    fieldWithPath("reason").description("?????? ?????? ?????? ?????? ( 1000??? ?????? )"),
                    fieldWithPath("origin").description("?????? ??????"),
                    fieldWithPath("info").description("?????? ??????"),
                    fieldWithPath("imageUrls").description("?????? ????????? ?????? ?????? ?????? ????????? ( ?????? 5??? )"),
                    fieldWithPath("city").description("???/???"),
                    fieldWithPath("street").description("???????????????"),
                    fieldWithPath("zipcode").description("????????????")
                ),
                responseFields(
                    fieldWithPath("status").description("?????? ??????"),
                    fieldWithPath("message").description("?????? ?????????"),
                    fieldWithPath("data").description("?????? ????????? ID")
                )
            )
        );
  }
}