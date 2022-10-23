package com.kusitms.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class ImageFile extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "image_id")
  private Long id;

  private String imageUrl;

  public static ImageFile toEntity(String imageUrl) {
    return ImageFile.builder()
        .imageUrl(imageUrl)
        .build();
  }
}
