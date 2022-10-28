package com.kusitms.backend.controller;

import com.kusitms.backend.dto.RegionDto;
import com.kusitms.backend.response.BaseResponse;
import com.kusitms.backend.service.IRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
}
