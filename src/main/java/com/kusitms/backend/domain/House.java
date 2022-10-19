package com.kusitms.backend.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class House extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "house_id")
  private Long id;

  @Embedded
  private Address address; // 도로명 주소
  private String price; // 구체적 가격
  private String size; // 집 크기
  private LocalDateTime createdDate; // 준공연도
  private String purpose; // 용도

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private User owner;
}
