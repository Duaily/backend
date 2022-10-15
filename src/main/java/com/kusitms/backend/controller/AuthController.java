package com.kusitms.backend.controller;

import com.kusitms.backend.dto.AuthDto;
import com.kusitms.backend.service.AuthService;
import javax.validation.Valid;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  //회원가입
  @PostMapping("/sign-up")
  public ResponseEntity<Result> signUp(@Valid @RequestBody AuthDto.Request request) {

    AuthDto.Response response = authService.signUp(request);

    return ResponseEntity.ok(Result.builder().status(HttpStatus.OK.value())
        .message("회원가입에 성공하셨습니다.").data(response).build());
  }

  //닉네임 중복 확인
  @GetMapping("/checkNickname/{nickname}")
  public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname) {
    return ResponseEntity.ok(authService.checkNicknameDuplication(nickname));
  }

  //회원 탈퇴
  @DeleteMapping("/withdrawal/{userId}")
  public ResponseEntity<Result> withdrawal(@PathVariable int userId) {
    //현재 로그인한 회원의 userId 받아오도록 해야하는데 일단 매개변수로
    authService.withdrawal(userId);

    return ResponseEntity.ok(Result.builder().status(HttpStatus.OK.value())
        .message("회원 탈퇴에 성공하셨습니다.").build());
  }

  @Data
  @Builder
  static class Result {
    private Integer status;
    private String message;
    private Object data;
  }
}
