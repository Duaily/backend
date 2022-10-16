package com.kusitms.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "user_id")
  private Long id;

  private String nickname;
  private String password;
  private String contact;
  private String email;
  private String address;

  @Enumerated(EnumType.STRING)
  private Authority authority;
}
