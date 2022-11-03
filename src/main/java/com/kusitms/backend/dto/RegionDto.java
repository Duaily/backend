package com.kusitms.backend.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegionDto {

  private String title;
  private String content; // 지역 게시글 내용
  private String name; // 지역명
  private String reason; // 텍스트 1000자 이내 ( 지역 간단 추천 이유 )
  private String origin; // 지역 유래
  private String info;
  private String city;
  private String street;
  private String zipcode;
  private List<String> imageUrls;
}
