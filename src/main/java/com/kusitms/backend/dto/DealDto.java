package com.kusitms.backend.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class DealDto {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    private Long postId;
  }

}
