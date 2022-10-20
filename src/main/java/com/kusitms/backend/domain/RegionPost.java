package com.kusitms.backend.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@DiscriminatorValue("regionPost")
@Getter
@Entity
public class RegionPost extends Post {

  private String content;

  @OneToOne
  private Region region;
}
