package com.kusitms.backend.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Getter
@Entity
public abstract class Post extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "post_id")
  private Long id;

  @Size(max = 20)
  private String title; // 제목
  private String imageUrl; // 대표사진

  @ManyToMany(mappedBy = "post")
  private Set<User> writer;

  @OneToMany
  @JoinColumn(name = "post_id")
  private Set<Comment> comments;
}
