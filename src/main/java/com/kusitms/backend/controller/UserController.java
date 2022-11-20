package com.kusitms.backend.controller;

import com.kusitms.backend.config.SecurityUtil;
import com.kusitms.backend.dto.UserDto;
import com.kusitms.backend.response.BaseResponse;
import com.kusitms.backend.service.IHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
  private final IHouseService houseService;

  @GetMapping
  public ResponseEntity<BaseResponse> getUserInfo() {
    UserDto response = houseService.getUserInfo(SecurityUtil.getCurrentUserId());
    return ResponseEntity.ok(BaseResponse.builder()
        .data(response).message("유저 정보 조회 성공").build());
  }
}
