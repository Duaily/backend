package com.kusitms.backend.controller;

import com.kusitms.backend.config.SecurityUtil;
import com.kusitms.backend.domain.HousePost;
import com.kusitms.backend.dto.DealDto;
import com.kusitms.backend.dto.HouseDto;
import com.kusitms.backend.dto.HousePreviewDto;
import com.kusitms.backend.response.BaseResponse;
import com.kusitms.backend.response.PageResponse;
import com.kusitms.backend.service.IHouseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/house")
public class HouseController {

  private final IHouseService houseService;

  @GetMapping("/list")
  public ResponseEntity<PageResponse> getHousePostList(
      @RequestParam(name = "page", defaultValue = "1") Integer page
  ) {
    List<HousePreviewDto> response = houseService.getHousePostList(PageRequest.of(page - 1, 8));

    int totalCount = houseService.getHousePostCount() / 8 + 1;

    return ResponseEntity.ok(PageResponse.builder()
        .data(response).message("빈 집 게시글 목록 조회를 성공하셨습니다.")
        .page(page).totalCount(totalCount).build());
  }

  @PostMapping
  public ResponseEntity<BaseResponse> create(@RequestBody HouseDto request) {

    Long response = houseService.create(SecurityUtil.getCurrentUserId(), request);
    return ResponseEntity.ok(BaseResponse.builder()
        .message("빈 집 게시글 생성 성공").data(response).build());
  }

  @PostMapping("/deal")
  public ResponseEntity<BaseResponse> createDeal(@RequestBody DealDto.Request request) {

    Long response = houseService.createDeal(request, SecurityUtil.getCurrentUserId());
    return ResponseEntity.ok(BaseResponse.builder()
        .data(response).message("거래가 성공적으로 시작되었습니다.").build());
  }

  @PutMapping("/deal/{dealId}")
  public ResponseEntity<BaseResponse> modifyDeal(@PathVariable Long dealId) {

    Long response = houseService.modifyDeal(dealId, SecurityUtil.getCurrentUserId());
    return ResponseEntity.ok(BaseResponse.builder()
        .data(response).message("거래가 성공적으로 완료되었습니다.").build());
  }

  @GetMapping("/{houseId}")
  public ResponseEntity<BaseResponse> getDetail(@PathVariable Long houseId) {
    HousePost response = houseService.getDetail(houseId);
    return ResponseEntity.ok(BaseResponse.builder()
        .data(response).message("상세 조회 성공").build());
  }
}
