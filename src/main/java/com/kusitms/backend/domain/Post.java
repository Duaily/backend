package com.kusitms.backend.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Post extends MetaEntity {

  @Id
  @GeneratedValue
  private Long id;

  private String title; // 제목
  private String imageUrl; // 대표사진

}
