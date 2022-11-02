package com.kusitms.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class HouseDto {

  private String title; // 제목
  private List<String> imageUrls; // 사진 최대 5장
  private String city;
  private String street;
  private String zipcode;
  private String price; // 구체적 가격
  private String size; // 집 크기
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
  private LocalDate createdDate; // 준공연도
  private String purpose; // 용도
  private Long regionId; // 지역 Id
}
