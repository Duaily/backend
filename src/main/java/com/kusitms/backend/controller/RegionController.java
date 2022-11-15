package com.kusitms.backend.controller;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import com.kusitms.backend.domain.HousePost;
import com.kusitms.backend.domain.RegionPost;
import com.kusitms.backend.dto.RegionDto;
import com.kusitms.backend.response.BaseResponse;
import com.kusitms.backend.service.IRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/region")
@RequiredArgsConstructor
public class RegionController {

  private final IRegionService regionService;

  @PostMapping
  public ResponseEntity<BaseResponse> create(@RequestBody RegionDto request) {
    Long response = regionService.create(request);
    return ResponseEntity.ok(BaseResponse.builder()
        .message("지역 생성 성공").data(response).build());
  }

  @GetMapping("/{regionId}")
  public ResponseEntity<BaseResponse> getDetail(@PathVariable Long regionId) {
    RegionPost response = regionService.getDetail(regionId);
    return ResponseEntity.ok(BaseResponse.builder()
        .data(response).message("상세 조회 성공").build());

  }

}
