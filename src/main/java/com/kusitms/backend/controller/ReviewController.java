package com.kusitms.backend.controller;

import com.kusitms.backend.config.SecurityUtil;
import com.kusitms.backend.domain.Post;
import com.kusitms.backend.domain.ReviewPost;
import com.kusitms.backend.dto.ReviewPostDto;
import com.kusitms.backend.response.BaseResponse;
import com.kusitms.backend.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping
  public ResponseEntity<BaseResponse> create(@RequestBody ReviewPostDto request) {

    Long response = reviewService.create(SecurityUtil.getCurrentUserId(), request);
    return ResponseEntity.ok(BaseResponse.builder()
        .message("후기 게시글 생성 성공").data(response).build());
  }

  @GetMapping("/{postId}")
  public ResponseEntity<BaseResponse> getReviewDetail(@PathVariable Long postId) {

    Post reviewPost = reviewService.getReviewDetail(postId);
    return ResponseEntity.ok(BaseResponse.builder()
        .message("후기 게시글 상세 조회 성공").data(reviewPost).build());

  }

  @GetMapping("/list")
  public ResponseEntity<BaseResponse> getReviewList() {

    return ResponseEntity.ok(BaseResponse.builder()
        .message("후기 게시글 목록 조회 성공").data(reviewService.getReviewList()).build());
  }
}
