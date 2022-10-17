package com.kusitms.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class ReviewPost {

  @Id
  @GeneratedValue
  @Column(name = "review_post_id")
  private Long id;

  private String previewText; // 미리보기
  private String content; // 게시물 내용

  @Builder
  public ReviewPost(String previewText, String content) {
    this.previewText = previewText;
    this.content = content;
  }

}