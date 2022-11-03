package com.kusitms.backend.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@DiscriminatorValue("reviewPost")
@Getter
@Setter
@Entity
public class ReviewPost extends Post {

  @Size(max = 200)
  private String previewText; // 미리보기
  private String content; // 게시물 내용

  @OneToMany(cascade = CascadeType.ALL)
  private static Set<ImageFile> imageFileSet;

  public static ReviewPost createReviewPost(String title, String content,
      List<String> imageUrls, User user) {

    imageFileSet = imageUrls.stream().map(ImageFile::toEntity).collect(Collectors.toSet());

    ReviewPost reviewPost = new ReviewPost();
    reviewPost.setTitle(title);
    reviewPost.setContent(content);
    reviewPost.setImageFile(imageFileSet.stream().findFirst().orElse(null));
    reviewPost.setUser(user);
    reviewPost.setPreviewText(content); // 글자 수 다시 보기
    return reviewPost;
  }
}