package com.kusitms.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Deal extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "deal_id")
  private Long id;

  @OneToOne
  private User buyer;

  @OneToOne
  private Post housePost;

}
