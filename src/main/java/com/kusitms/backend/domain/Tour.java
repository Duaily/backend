package com.kusitms.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Tour extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "tour_id")
  private Long id;

  private String name; // 관광지명
  private String content; // 간단소개

  @OneToOne
  private ImageFile imageFile; // 대표 이미지

}
