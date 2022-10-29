package com.kusitms.backend.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "user_id")
  private Long id;
  @Column(nullable = false, unique = true)
  private String nickname;

  private String password;

  private String contact;
  @Column(nullable = false)
  private String email;

  private int status; // 탈퇴여부 default 0 || exit 1

  @Enumerated(EnumType.STRING)
  private Authority authority;

  @OneToMany(mappedBy = "owner")
  private Set<House> houses;

  @OneToMany(mappedBy = "user")
  @JsonManagedReference
  private List<Post> posts;

  @OneToMany
  @JoinColumn(name = "commenter_id")
  private Set<Comment> comments;
}
