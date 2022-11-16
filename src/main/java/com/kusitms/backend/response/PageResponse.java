package com.kusitms.backend.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse extends BaseResponse {
  private int page; // 현재 페이지
  private int totalCount; // 총 페이지 수
}
