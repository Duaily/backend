package com.kusitms.backend.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@DiscriminatorValue("reviewPost")
@Getter
@Entity
public class ReviewPost extends Post {

  @Size(max = 200)
  private String previewText; // 미리보기
  private String content; // 게시물 내용

}