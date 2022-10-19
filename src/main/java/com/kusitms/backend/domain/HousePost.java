package com.kusitms.backend.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@DiscriminatorValue("housePost")
@Getter
@Entity
public class HousePost extends Post {

  @OneToOne
  private House house;

  @OneToOne
  private Region region;
}
