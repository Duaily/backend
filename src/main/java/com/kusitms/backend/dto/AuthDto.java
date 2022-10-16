package com.kusitms.backend.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

public class AuthDto {
  @Data
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
    @NotBlank
    String contact;
  }

  @Data
  public static class Response {
    @NotBlank String loginId;
    @NotBlank String email;
    @NotBlank String nickname;
  }

}
