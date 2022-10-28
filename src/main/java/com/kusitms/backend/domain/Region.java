package com.kusitms.backend.domain;

import com.kusitms.backend.dto.RegionDto;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Region extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "region_id")
  private Long id;
  private String name; // 지역명
  private String reason; // 간단 추천 이유
  private String origin; // 지역 유래
  private String info; // 지역 정보

  @Embedded
  private Address address; // 지역 위치

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "region_id")
  private Set<ImageFile> imageFileSet;

  public static Region createRegion(RegionDto request) {
    return Region.builder()
        .reason(request.getReason())
        .imageFileSet(request.getImageUrls()
            .stream()
            .map(ImageFile::toEntity)
            .collect(Collectors.toSet()))
        .build();
  }
}
