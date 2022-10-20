package com.kusitms.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Notice extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "notice_id")
  private Long id;

  private String code; // 매매 이전, 체결, 이후
  private String content; // 공지 내용

  @Builder
  public Notice(String code, String content) {
    this.code = code;
    this.content = content;
  }
}
