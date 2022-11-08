package com.kusitms.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

  private String grantType;
  private String accessToken;
  private String refreshToken;
  private Long accessTokenExpireTime;
  private boolean isInit;
}
