package com.kusitms.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckSmsRequest {

  private String code;
  private String contact;
}
