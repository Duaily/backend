package com.kusitms.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Deal extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "deal_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  private DealState state;

  @OneToOne
  private User buyer;

  @OneToOne
  private HousePost housePost;

  public static Deal createDeal(User buyer, HousePost housePost) {
    return Deal.builder()
        .buyer(buyer)
        .housePost(housePost)
        .state(DealState.ONGOING)
        .build();
  }

  public void modifyStatus() {
    this.state = DealState.COMPLETED;
  }

  public boolean isCompleted() {
    return state == DealState.COMPLETED;
  }
}
