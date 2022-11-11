package com.kusitms.backend.controller;

import static com.kusitms.backend.ApiDocumentUtils.getDocumentRequest;
import static com.kusitms.backend.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kusitms.backend.config.JwtAccessDeniedHandler;
import com.kusitms.backend.config.JwtAuthenticationEntryPoint;
import com.kusitms.backend.config.JwtSecurityConfig;
import com.kusitms.backend.config.TokenProvider;
import com.kusitms.backend.dto.DealDto;
import com.kusitms.backend.dto.HouseDto;
import com.kusitms.backend.dto.HousePreviewDto;
import com.kusitms.backend.repository.DealRepository;
import com.kusitms.backend.repository.HouseRepository;
import com.kusitms.backend.repository.PostRepository;
import com.kusitms.backend.repository.RegionRepository;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.service.IHouseService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
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
@WebMvcTest(HouseController.class)
@MockBean(JpaMetamodelMappingContext.class)
class HouseControllerTest {

  // static data
  final String email = "test@test.com";
  @Autowired
  MockMvc mockMvc;
  @MockBean
  IHouseService houseService;
  @MockBean
  HouseRepository houseRepository;
  @MockBean
  PostRepository postRepository;
  @MockBean
  UserRepository userRepository;
  @MockBean
  RegionRepository regionRepository;
  @MockBean
  DealRepository dealRepository;
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
  @DisplayName("빈 집 게시글 생성")
  void create() throws Exception {
    // request body
    final HouseDto request = HouseDto.builder()
        .title("속초 오션뷰 하우스를 소개합니다.")
        .imageUrls(List.of(
            "image1.address",
            "image2.address",
            "image3.address"))
        .city("강원도")
        .street("속초시")
        .zipcode("12345")
        .price("150000000")
        .createdDate(LocalDate.parse("2015-12-09"))
        .purpose("게스트 하우스")
        .regionId(1L)
        .build();

    final Long response = 1L;

    // get user data from security context
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(email);

    given(houseService.create(email, request)).willReturn(response);

    String json = objectMapper.writeValueAsString(request);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .post("/api/house")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .characterEncoding("utf-8")
            .accept(MediaType.APPLICATION_JSON)
    );

    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("create-house", getDocumentRequest(), getDocumentResponse(),
                requestFields(
                    fieldWithPath("title").description("빈 집 게시글 제목"),
                    fieldWithPath("imageUrls").description("빈 집 게시글 첨부 사진 주소 리스트 ( 최대 5장 )"),
                    fieldWithPath("city").description("도/시"),
                    fieldWithPath("street").description("도로명주소"),
                    fieldWithPath("zipcode").description("우편번호"),
                    fieldWithPath("price").description("빈 집 가격( 정확한 금액 )"),
                    fieldWithPath("size").description("빈 집 크기"),
                    fieldWithPath("createdDate").description("준공연도 (yyyy-MM-dd)"),
                    fieldWithPath("purpose").description("빈 집 용도"),
                    fieldWithPath("regionId").description("지역 ID")
                ),
                responseFields(
                    fieldWithPath("status").description("결과 코드"),
                    fieldWithPath("message").description("응답 메세지"),
                    fieldWithPath("data").description("빈 집 게시글 ID")
                )
            )
        );
  }

  @Test
  @DisplayName("빈 집 거래 생성")
  void createDeal() throws Exception {
    // request data
    final DealDto.Request request = DealDto.Request
        .builder()
        .postId(1L)
        .build();

    // response data
    final Long response = 1L;

    // get user data from security context
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(email);

    given(houseService.createDeal(request, email)).willReturn(response);

    String json = objectMapper.writeValueAsString(request);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .post("/api/house/deal")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .characterEncoding("utf-8")
            .accept(MediaType.APPLICATION_JSON)
    );

    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("create-deal", getDocumentRequest(), getDocumentResponse(),
                requestFields(
                    fieldWithPath("postId").description("빈 집 게시글 ID")
                ),
                responseFields(
                    fieldWithPath("status").description("결과 코드"),
                    fieldWithPath("message").description("응답 메세지"),
                    fieldWithPath("data").description("빈 집 거래 ID")
                )
            )
        );
  }

  @Test
  @DisplayName("거래 완료")
  void modifyDeal() throws Exception {
    // request and response data
    final Long dealId = 1L;

    // get user data from security context
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(email);

    given(houseService.modifyDeal(dealId, email)).willReturn(dealId);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .put("/api/house/deal/{dealId}", dealId)
            .characterEncoding("utf-8")
            .accept(MediaType.APPLICATION_JSON)
    );

    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("modify-deal", getDocumentRequest(), getDocumentResponse(),
                pathParameters(
                    parameterWithName("dealId").description("빈 집 거래 ID")
                ),
                responseFields(
                    fieldWithPath("status").description("결과 코드"),
                    fieldWithPath("message").description("응답 메세지"),
                    fieldWithPath("data").description("빈 집 거래 ID")
                )
            )
        );
  }

  @Test
  @DisplayName("빈 집 게시글 목록 조회")
  void getHousePostList() throws Exception {
    // request data
    final Integer page = 1;
    // response data
    final List<HousePreviewDto> response = new ArrayList<>();
    final HousePreviewDto house1 = HousePreviewDto.builder()
        .postId(1L)
        .title("어서오세요. 이곳은 전경이 아름다운 속초 앞바다 빈 집 입니다.")
        .imageUrl("image address")
        .author("Dual ( 작성자 닉네임 )")
        .minPrice(100000000)
        .maxPrice(200000000)
        .location("강원도 속초시")
        .build();
    final HousePreviewDto house2 = HousePreviewDto.builder()
        .postId(2L)
        .title("제주의 자연을 느끼며 .... ")
        .imageUrl("image address")
        .author("농부 ( 작성자 닉네임 )")
        .minPrice(400000000)
        .maxPrice(500000000)
        .location("제주도 서귀포시")
        .build();
    final HousePreviewDto house3 = HousePreviewDto.builder()
        .postId(3L)
        .title("전주의 먹거리를 집 앞에서 ..!! ")
        .imageUrl("image address")
        .author("국수조아 ( 작성자 닉네임 )")
        .minPrice(300000000)
        .maxPrice(400000000)
        .location("전라도 전주시")
        .build();
    response.add(house1);
    response.add(house2);
    response.add(house3);

    given(houseService.getHousePostList(PageRequest.of(page - 1, 8))).willReturn(response);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .get("/api/house/list?page={page}", page)
    );
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("house-list", getDocumentRequest(), getDocumentResponse(),
                requestParameters(
                    parameterWithName("page").description("페이지 번호")
                ),
                responseFields(
                    fieldWithPath("status").description("결과 코드"),
                    fieldWithPath("message").description("응답 메세지"),
                    fieldWithPath("data.[].postId").description("빈 집 게시글 ID"),
                    fieldWithPath("data.[].title").description("게시글 제목"),
                    fieldWithPath("data.[].location").description("빈 집 주소"),
                    fieldWithPath("data.[].imageUrl").description("대표 이미지 url"),
                    fieldWithPath("data.[].minPrice").description("최소 가격"),
                    fieldWithPath("data.[].maxPrice").description("최대 가격"),
                    fieldWithPath("data.[].author").description("작성자 닉네임")
                ))
        );

  }
}