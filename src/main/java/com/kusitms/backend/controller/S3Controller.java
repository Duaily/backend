package com.kusitms.backend.controller;

import com.kusitms.backend.response.BaseResponse;
import com.kusitms.backend.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {

  private final S3Service s3Service;

  @PostMapping
  public ResponseEntity<BaseResponse> upload(
      @RequestPart("file") MultipartFile multipartFile) {
    String imageUrl = s3Service.upload(multipartFile);
    return ResponseEntity.ok(BaseResponse.builder()
        .message("이미지 업로드 성공").data(imageUrl).build());
  }
}
