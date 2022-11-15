package com.kusitms.backend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse {
  private final Integer status = HttpStatus.OK.value();

  private String message;

  @JsonProperty
  private Object data;

  private int page; // 현재 페이지

  private int totalCount; // 총 페이지 수
}
