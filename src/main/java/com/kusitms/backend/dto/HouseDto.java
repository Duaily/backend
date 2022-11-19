package com.kusitms.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HouseDto {

  private String title; // 제목
  private List<String> imageUrls; // 사진 최대 5장
  private String city;
  private String street;
  private String zipcode;
  private String cost; // 가격
  private String size; // 집 크기
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
  private LocalDate createdDate; // 준공연도
  private String purpose; // 용도
  private String deposit; // 보증금
  private String category; // 카테고리(매매/월세)
  private Long regionId; // 지역 Id

  @Data
  @Builder
  public static class Response {
    private String title; // 제목
    private String location; // 위치
    private List<String> imageUrls; // 이미지 URL
    private String size; // 크기
    private String purpose; // 용도
    private Integer minPrice; // 최소 가격(매매)
    private Integer maxPrice; // 최대 가격(매매)
    private String cost; // 가격(월세)
    private String deposit; // 월세 보증금(월세)
    private String author; // 작성자 닉네임
    private String contact; // 작성자 전화번호
    private boolean isPossible; // 입주 가능
  }
}


