package com.kusitms.backend.dto;

import com.kusitms.backend.domain.Comment;
import com.kusitms.backend.domain.ImageFile;
import com.kusitms.backend.domain.Post;
import com.kusitms.backend.domain.ReviewPost;
import com.kusitms.backend.domain.User;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewPostDto {
  @Data
  @Builder
  public static class Request {
    private String title; // 제목
    private String content; // 내용
    private List<String> imageUrls; // 사진
  }

  @Data
  @Builder
  public static class Response {
    private String title; // 제목
    private String content; // 내용
    private String imageUrl; // 사진
    private Set<Comment> comments; // 댓글
  }

  @Data
  @Builder
  public static class PreviewDto {
    private String title; // 제목
    private String previewText; // 미리보기
    private String user; // 작성자
    private String imageUrl; // 사진

    public static ReviewPostDto.PreviewDto reviewResponse(ReviewPost post) {
      return ReviewPostDto.PreviewDto.builder().title(post.getTitle())
          .previewText(post.getPreviewText()).user(post.getUser().getNickname())
          .imageUrl(post.getImageFile().getImageUrl()).build();
    }
  }
}
