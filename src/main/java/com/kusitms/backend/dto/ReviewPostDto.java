package com.kusitms.backend.dto;

import com.kusitms.backend.domain.ImageFile;
import com.kusitms.backend.domain.Post;
import com.kusitms.backend.domain.ReviewPost;
import com.kusitms.backend.domain.User;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
public class ReviewPostDto {

  private String title; // 제목
  private String content; // 내용
  private List<String> imageUrls; // 사진

  @Data
  @Builder
  public static class Response {
    private String title; // 제목
    private String previewText; // 미리보기
    private String user; // 작성자
    private ImageFile imageUrl; // 사진


    public static ReviewPostDto.Response reviewResponse(ReviewPost post) {
      return Response.builder().title(post.getTitle())
          .previewText(post.getPreviewText()).user(post.getUser().getNickname())
          .imageUrl(post.getImageFile()).build();
    }
  }
}
