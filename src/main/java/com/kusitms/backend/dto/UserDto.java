package com.kusitms.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

  private String email;
  private String nickname;
  private String contact;

}
