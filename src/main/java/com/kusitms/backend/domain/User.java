package com.kusitms.backend.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String contact;
  @Column(nullable = false)
  private String email;
  private int status; // 탈퇴여부 default 0 || exit 1

  @Enumerated(EnumType.STRING)
  private Authority authority;

  @OneToMany(mappedBy = "owner")
  private Set<House> houses;

  @ManyToMany
  @JoinTable(name = "writer_post",
      joinColumns = @JoinColumn(name = "writer_id"),
      inverseJoinColumns = @JoinColumn(name = "post_id"))
      private Set<Post> posts;

  @OneToMany
  @JoinColumn(name = "commenter_id")
  private Set<Comment> comments;
}
