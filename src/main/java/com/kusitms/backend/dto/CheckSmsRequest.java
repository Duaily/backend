package com.kusitms.backend.dto;

import lombok.Data;

@Data
public class CheckSmsRequest {

  private String code;
  private String contact;
}
