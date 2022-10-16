package com.kusitms.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends MetaEntity {

  @Id
  @GeneratedValue
  private Long id;
  @Column(nullable = false, unique = true)
  private String nickname;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String contact;
  @Column(nullable = false)
  private String email;

  @Enumerated(EnumType.STRING)
  private Authority authority;

  @Builder
  public User(String nickname, String contact, String email, String password, Authority authority) {
    this.nickname = nickname;
    this.contact = contact;
    this.email = email;
    this.password = password;
    this.authority = authority;
  }
}
