package com.kusitms.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Comment extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "comment_id")
  private Long id;
  private String content;

  public Comment() {}

}
