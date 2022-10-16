package com.kusitms.backend.controller;

import com.kusitms.backend.dto.SignInRequest;
import com.kusitms.backend.response.BaseResponse;
import com.kusitms.backend.service.AuthService;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/sign-in")
  public ResponseEntity<BaseResponse> signIn(@RequestBody @Validated SignInRequest request,
      HttpServletResponse response) {
    String accessToken = authService.signIn(request);
    response.addHeader("Authorization", "Bearer " + accessToken);
    return ResponseEntity.ok(BaseResponse.builder().message("로그인에 성공하셨습니다.").build());
  }
}
