package com.kusitms.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Getter
@Setter
@Entity
public abstract class Post extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "post_id")
  private Long id;

  @Size(max = 50)
  private String title; // 제목

  @OneToOne
  private ImageFile imageFile; // 대표사진

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany
  @JoinColumn(name = "post_id")
  private Set<Comment> comments;
}
