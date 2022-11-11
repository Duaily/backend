package com.kusitms.backend.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

public class AuthDto {
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 8글자 이상 입력해주세요.")
    String password;
    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9._+-]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9.]+$",
        message = "올바른 이메일 주소를 입력해주세요.")
    String email;
    @NotBlank(message = "닉네임을 입력해주세요.")
    String nickname;
    @NotBlank(message = "전화번호를 입력해주세요.")
    String contact;
  }

  @Data
  @Builder
  public static class Response {
    @NotBlank String nickname;
  }

}