package com.kusitms.backend.domain;

import com.kusitms.backend.dto.RegionDto;
import java.util.stream.Collectors;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@DiscriminatorValue("regionPost")
@Getter
@Setter
@Entity
public class RegionPost extends Post {

  private String content; // 한 줄 가치 표현

  @OneToOne
  private Region region;

  public static RegionPost createRegionPost(Region savedRegion, RegionDto request) {
    RegionPost regionPost = new RegionPost();
    regionPost.setRegion(savedRegion);
    regionPost.setTitle(request.getTitle());
    regionPost.setContent(request.getContent());
    regionPost.setImageFile(savedRegion.getImageFileSet().stream().findFirst().orElse(null));

    // 어드민 유저 추가하기
    return regionPost;
  }
}
