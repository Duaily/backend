package com.kusitms.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Entity
public class House extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "house_id")
  private Long id;

  private String address; // 도로명 주소
  private String price; // 구체적 가격
  private String size;
  private LocalDateTime createdDate;
  private String purpose; // 용도

  public House() {}

}
