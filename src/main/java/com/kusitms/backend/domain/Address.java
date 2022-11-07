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
public class Address {

  @Column
  private String city;

  @Column
  private String street;

  @Column
  private String zipcode;

}
