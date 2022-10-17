package com.kusitms.backend.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "user_id")
  private Long id;
  @Column(nullable = false, unique = true)
  private String nickname;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String contact;
  @Column(nullable = false)
  private String email;
  private int status; // 탈퇴여부 default 0 || exit 1

  @Enumerated(EnumType.STRING)
  private Authority authority;

  @Builder
  public User(String nickname, String contact, String email, String password, Authority authority) {
    this.nickname = nickname;
    this.contact = contact;
    this.email = email;
    this.password = password;
    this.authority = authority;
    this.status = 0;
  }
}
