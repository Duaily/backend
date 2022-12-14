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
import com.kusitms.backend.response.PageInfo;
import com.kusitms.backend.response.PageResponse;
import com.kusitms.backend.service.IHouseService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

  // static data
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
  @DisplayName("??? ??? ????????? ?????? - ??????")
  void create_Mine() throws Exception {
    // request body
    final HouseDto.Request request = HouseDto.Request.builder()
        .title("?????? ????????? ???????????? ???????????????.")
        .imageUrls(List.of(
            "image1.address",
            "image2.address",
            "image3.address"))
        .city("?????????")
        .street("?????????")
        .zipcode("12345")
        .size("2435000000")
        .cost("150000000")
        .category("MINE")
        .createdDate(LocalDate.parse("2015-12-09"))
        .purpose("????????? ?????????")
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
            document("create-house-mine", getDocumentRequest(), getDocumentResponse(),
                requestFields(
                    fieldWithPath("title").description("??? ??? ????????? ??????"),
                    fieldWithPath("imageUrls").description("??? ??? ????????? ?????? ?????? ?????? ????????? ( ?????? 5??? )"),
                    fieldWithPath("city").description("???/???"),
                    fieldWithPath("street").description("???????????????"),
                    fieldWithPath("zipcode").description("????????????"),
                    fieldWithPath("cost").description("??? ??? ??????( ????????? ?????? )"),
                    fieldWithPath("size").description("??? ??? ??????"),
                    fieldWithPath("category").description("???????????? (??????/??????)"),
                    fieldWithPath("createdDate").description("???????????? (yyyy-MM-dd)"),
                    fieldWithPath("purpose").description("??? ??? ??????"),
                    fieldWithPath("regionId").description("?????? ID"),
                    fieldWithPath("deposit").description("????????? ( -> ??????????????? ????????? ?????? )")
                ),
                responseFields(
                    fieldWithPath("status").description("?????? ??????"),
                    fieldWithPath("message").description("?????? ?????????"),
                    fieldWithPath("data").description("??? ??? ????????? ID")
                )
            )
        );
  }

  @Test
  @DisplayName("??? ??? ????????? ?????? - ??????")
  void create_Monthly() throws Exception {
    // request body
    final HouseDto.Request request = HouseDto.Request.builder()
        .title("????????? ????????? ????????? ??????????????????.")
        .imageUrls(List.of(
            "image1.address",
            "image2.address",
            "image3.address"))
        .city("?????????")
        .street("?????????")
        .zipcode("54321")
        .size("24350000124")
        .cost("800,000")
        .category("MONTHLY")
        .createdDate(LocalDate.parse("2018-11-09"))
        .purpose("?????? ??????")
        .deposit("1,000,000")
        .regionId(2L)
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
            document("create-house-monthly", getDocumentRequest(), getDocumentResponse(),
                requestFields(
                    fieldWithPath("title").description("??? ??? ????????? ??????"),
                    fieldWithPath("imageUrls").description("??? ??? ????????? ?????? ?????? ?????? ????????? ( ?????? 5??? )"),
                    fieldWithPath("city").description("???/???"),
                    fieldWithPath("street").description("???????????????"),
                    fieldWithPath("zipcode").description("????????????"),
                    fieldWithPath("cost").description("??? ??? ?????? ?????? ?????? ( ????????? ?????? )"),
                    fieldWithPath("size").description("??? ??? ??????"),
                    fieldWithPath("category").description("???????????? (??????/??????)"),
                    fieldWithPath("createdDate").description("???????????? (yyyy-MM-dd)"),
                    fieldWithPath("purpose").description("??? ??? ??????"),
                    fieldWithPath("regionId").description("?????? ID"),
                    fieldWithPath("deposit").description("????????? ( -> ??????????????? ????????? ?????? )")
                ),
                responseFields(
                    fieldWithPath("status").description("?????? ??????"),
                    fieldWithPath("message").description("?????? ?????????"),
                    fieldWithPath("data").description("??? ??? ????????? ID")
                )
            )
        );
  }

  @Test
  @DisplayName("??? ??? ?????? ??????")
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
                    fieldWithPath("postId").description("??? ??? ????????? ID")
                ),
                responseFields(
                    fieldWithPath("status").description("?????? ??????"),
                    fieldWithPath("message").description("?????? ?????????"),
                    fieldWithPath("data").description("??? ??? ?????? ID")
                )
            )
        );
  }

  @Test
  @DisplayName("?????? ??????")
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
                    parameterWithName("dealId").description("??? ??? ?????? ID")
                ),
                responseFields(
                    fieldWithPath("status").description("?????? ??????"),
                    fieldWithPath("message").description("?????? ?????????"),
                    fieldWithPath("data").description("??? ??? ?????? ID")
                )
            )
        );
  }

  @Test
  @DisplayName("??? ??? ????????? ?????? ??????")
  void getHousePostList() throws Exception {
    // request data
    final Integer page = 1;
    // response data
    final List<HousePreviewDto> response = new ArrayList<>();
    final HousePreviewDto house1 = HousePreviewDto.builder()
        .postId(1L)
        .title("???????????????. ????????? ????????? ???????????? ?????? ????????? ??? ??? ?????????.")
        .imageUrl("image address")
        .author("Dual ( ????????? ????????? )")
        .minPrice(100000000)
        .maxPrice(200000000)
        .deposit("null")
        .cost("null")
        .location("????????? ?????????")
        .build();
    final HousePreviewDto house2 = HousePreviewDto.builder()
        .postId(2L)
        .title("????????? ????????? ????????? .... ")
        .imageUrl("image address")
        .author("?????? ( ????????? ????????? )")
        .deposit("2000")
        .cost("50")
        .minPrice(0)
        .maxPrice(0)
        .location("????????? ????????????")
        .build();
    final HousePreviewDto house3 = HousePreviewDto.builder()
        .postId(3L)
        .title("????????? ???????????? ??? ????????? ..!! ")
        .imageUrl("image address")
        .author("???????????? ( ????????? ????????? )")
        .minPrice(300000000)
        .maxPrice(400000000)
        .deposit("null")
        .cost("null")
        .location("????????? ?????????")
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
                    parameterWithName("page").description("????????? ??????")
                ),
                responseFields(
                    fieldWithPath("status").description("?????? ??????"),
                    fieldWithPath("message").description("?????? ?????????"),
                    fieldWithPath("data.[].postId").description("??? ??? ????????? ID"),
                    fieldWithPath("data.[].title").description("????????? ??????"),
                    fieldWithPath("data.[].location").description("??? ??? ??????"),
                    fieldWithPath("data.[].imageUrl").description("?????? ????????? url"),
                    fieldWithPath("data.[].minPrice").description("?????? ?????? (????????? ??????)"),
                    fieldWithPath("data.[].maxPrice").description("?????? ?????? (????????? ??????)"),
                    fieldWithPath("data.[].deposit").description("????????? (????????? ??????)"),
                    fieldWithPath("data.[].cost").description("?????? (????????? ??????)"),
                    fieldWithPath("data.[].author").description("????????? ?????????"),
                    fieldWithPath("pageInfo.page").description("?????? ?????????"),
                    fieldWithPath("pageInfo.size").description("????????? ??? ????????? ??????"),
                    fieldWithPath("pageInfo.totalElements").description("??? ????????? ??????"),
                    fieldWithPath("pageInfo.totalPages").description("??? ????????? ???")
                ))
        );

  }

  @Test
  @DisplayName("?????? ????????? ??? ??? ????????? ?????? ??????")
  void getHousePostByMine() throws Exception {
    // request param
    final int page = 1;

    // response
    final List<HousePreviewDto> list = new ArrayList<>();
    final HousePreviewDto house1 = HousePreviewDto.builder()
        .postId(1L)
        .title("???????????????. ????????? ????????? ???????????? ?????? ????????? ??? ??? ?????????.")
        .imageUrl("image address")
        .author("Dual ( ????????? ????????? )")
        .minPrice(100000000)
        .maxPrice(200000000)
        .deposit("null")
        .cost("null")
        .location("????????? ?????????")
        .build();
    final HousePreviewDto house2 = HousePreviewDto.builder()
        .postId(2L)
        .title("????????? ????????? ????????? .... ")
        .imageUrl("image address")
        .author("Dual ( ????????? ????????? )")
        .deposit("2000")
        .cost("50")
        .minPrice(0)
        .maxPrice(0)
        .location("????????? ????????????")
        .cost("15000000")
        .deposit("50000000")
        .build();
    final HousePreviewDto house3 = HousePreviewDto.builder()
        .postId(3L)
        .title("????????? ???????????? ??? ????????? ..!! ")
        .imageUrl("image address")
        .author("Dual ( ????????? ????????? )")
        .minPrice(300000000)
        .maxPrice(400000000)
        .deposit("null")
        .cost("null")
        .location("????????? ?????????")
        .build();

    list.add(house1);
    list.add(house2);
    list.add(house3);

    final PageResponse response = PageResponse.builder()
        .pageInfo(PageInfo
            .builder()
            .page(page)
            .totalPages((list.size() / 8) + 1)
            .totalElements(list.size())
            .size(8)
            .build())
        .data(list)
        .build();

    // get user data from security context
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(email);

    // given
    given(houseService.getMineList(email, PageRequest.of(page - 1, 8))).willReturn(response);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .get("/api/house/mine?page={page}", page)
    );
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("house-list-mine", getDocumentRequest(), getDocumentResponse(),
                requestParameters(
                    parameterWithName("page").description("????????? ??????")
                ),
                responseFields(
                    fieldWithPath("status").description("?????? ??????"),
                    fieldWithPath("message").description("?????? ?????????"),
                    fieldWithPath("data.[].postId").description("??? ??? ????????? ID"),
                    fieldWithPath("data.[].title").description("????????? ??????"),
                    fieldWithPath("data.[].location").description("??? ??? ??????"),
                    fieldWithPath("data.[].imageUrl").description("?????? ????????? url"),
                    fieldWithPath("data.[].minPrice").description("?????? ?????? (????????? ??????)"),
                    fieldWithPath("data.[].maxPrice").description("?????? ?????? (????????? ??????)"),
                    fieldWithPath("data.[].deposit").description("????????? (????????? ??????)"),
                    fieldWithPath("data.[].cost").description("?????? (????????? ??????)"),
                    fieldWithPath("data.[].author").description("????????? ?????????"),
                    fieldWithPath("pageInfo.page").description("?????? ?????????"),
                    fieldWithPath("pageInfo.size").description("????????? ??? ????????? ??????"),
                    fieldWithPath("pageInfo.totalElements").description("??? ????????? ??????"),
                    fieldWithPath("pageInfo.totalPages").description("??? ????????? ???")
                ))
        );
  }

  @Test
  @DisplayName("??? ??? ????????? ?????? ??????")
  void getHouseDetail() throws Exception {
    final Long postId = 1L;

    HouseDto.Response response = HouseDto.Response.builder()
        .title("????????? ????????? ????????? ??????????????????.")
        .location("????????? ?????????")
        .imageUrls(List.of("image1.address", "image2.address"))
        .size("2435000000")
        .purpose("?????? ??????")
        .minPrice(null)
        .maxPrice(null)
        .cost("800,000")
        .deposit("1,000,000")
        .author("Dual")
        .contact("010-0000-0000")
        .isPossible(true)
        .build();

    given(houseService.getDetail(postId)).willReturn(response);

    ResultActions resultActions = mockMvc.perform(
        RestDocumentationRequestBuilders
            .get("/api/house/{postId}", postId)
    );

    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("house-detail", getDocumentRequest(), getDocumentResponse(),
                pathParameters(
                    parameterWithName("postId").description("????????? ID")
                ),
                responseFields(
                    fieldWithPath("status").description("?????? ??????"),
                    fieldWithPath("message").description("?????? ?????????"),
                    fieldWithPath("data.title").description("??? ??? ????????? ??????"),
                    fieldWithPath("data.location").description("??? ??? ??????"),
                    fieldWithPath("data.imageUrls").description("????????? url"),
                    fieldWithPath("data.size").description("??? ??? ??????"),
                    fieldWithPath("data.purpose").description("??? ??? ??????"),
                    fieldWithPath("data.cost").description("??? ??? ??????(????????? ??????)"),
                    fieldWithPath("data.deposit").description("??? ??? ?????????(????????? ??????)"),
                    fieldWithPath("data.minPrice").description("??? ??? ?????? ??????(????????? ??????)"),
                    fieldWithPath("data.maxPrice").description("??? ??? ?????? ??????(????????? ??????)"),
                    fieldWithPath("data.author").description("????????? ?????????"),
                    fieldWithPath("data.contact").description("????????? ????????????"),
                    fieldWithPath("data.possible").description("??? ??? ?????? ?????? ??????")
                )
            )
        );

  }

}