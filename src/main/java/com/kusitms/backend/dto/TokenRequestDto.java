package com.kusitms.backend.dto;

import lombok.Data;

@Data
public class TokenRequestDto {

  private String accessToken;
  private String refreshToken;
}
