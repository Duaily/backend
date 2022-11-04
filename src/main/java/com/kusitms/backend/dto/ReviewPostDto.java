package com.kusitms.backend.dto;

import java.util.List;
import lombok.Data;

@Data
public class ReviewPostDto {

  private String title; // 제목
  private String content; // 내용
  private List<String> imageUrls; // 사진

}
