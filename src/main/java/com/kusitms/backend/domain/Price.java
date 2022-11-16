package com.kusitms.backend.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Embeddable
public class Price {
  @Column
  private String cost; // 가격
  @Column
  private Category category; // 카테고리(매매, 월세)
  @Column
  private String deposit; // 보증금

}
