package com.kusitms.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Comment extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "comment_id")
  private Long id;
  private String content;

  @ManyToOne
  @JoinColumn(name = "commenter_id")
  private User commenter;
}
