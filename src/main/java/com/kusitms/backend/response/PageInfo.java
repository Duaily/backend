package com.kusitms.backend.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
  private int page; // 현재 페이지
  private int size; // 페이지 당 데이터 개수
  private int totalElements; // 총 데이터 개수
  private int totalPages; // 총 페이지 수
}
