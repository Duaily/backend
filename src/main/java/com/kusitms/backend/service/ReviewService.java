package com.kusitms.backend.service;

import com.kusitms.backend.domain.Post;
import com.kusitms.backend.domain.ReviewPost;
import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.ReviewPostDto;
import com.kusitms.backend.dto.ReviewPostDto.Response;
import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import com.kusitms.backend.repository.PostRepository;
import com.kusitms.backend.repository.ReviewPostRepository;
import com.kusitms.backend.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  private final ReviewPostRepository reviewPostRepository;

  // 후기 게시글 생성
  @Transactional
  public Long create(String email, ReviewPostDto.Request request) {
    // 현재 사용자 조회
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND_EXCEPTION));

    // 후기 게시글 생성
    ReviewPost reviewPost = ReviewPost.createReviewPost(request.getTitle(), request.getContent(),
        request.getImageUrls(), user);

    // 후기 게시글 저장
    ReviewPost saved = postRepository.save(reviewPost);

    // 후기 게시글 아이디 반환
    return saved.getId();

  }

  // 후기 게시글 상세 조회
  @Transactional
  public ReviewPostDto.Response getReviewDetail(Long postId) {
    ReviewPost reviewPost = (ReviewPost) postRepository.findById(postId)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND_EXCEPTION));

    return ReviewPostDto.Response.builder()
        .title(reviewPost.getTitle()).content(reviewPost.getContent())
        .imageUrl(reviewPost.getImageFile().getImageUrl())
        .comments(reviewPost.getComments()).build();
  }

  // 후기 게시글 목록 조회
  public List<ReviewPostDto.PreviewDto> getReviewPostList(Pageable page) {
    Page<ReviewPost> postList = reviewPostRepository.findAll(page);
    List<ReviewPostDto.PreviewDto> reviewDtoList = new ArrayList<>();

    for (ReviewPost post : postList) {
      reviewDtoList.add(ReviewPostDto.PreviewDto.reviewResponse(post));
    }

    Collections.reverse(reviewDtoList);
    return reviewDtoList;
  }
}


