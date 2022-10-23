package com.kusitms.backend.controller;

import com.kusitms.backend.config.SecurityUtil;
import com.kusitms.backend.dto.HouseDto;
import com.kusitms.backend.response.BaseResponse;
import com.kusitms.backend.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/house")
public class HouseController {

  private final HouseService houseService;

  @PostMapping
  public ResponseEntity<BaseResponse> create(@RequestBody HouseDto request) {

    Long response = houseService.create(SecurityUtil.getCurrentUserId(), request);
    return ResponseEntity.ok(BaseResponse.builder()
        .message("빈 집 게시글 생성 성공").data(response).build());
  }
}
