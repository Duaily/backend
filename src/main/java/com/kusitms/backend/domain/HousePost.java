package com.kusitms.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@DiscriminatorValue("housePost")
@Getter
@Setter
@Entity
public class HousePost extends Post {

  @OneToOne
  private House house;

  @OneToOne
  private Region region;

  public static HousePost createHousePost(House house, String title, List<String> imageUrls,
      User user, Region region) {
    HousePost housePost = new HousePost();
    housePost.setHouse(house);
    housePost.setTitle(title);
    housePost.setImageFile(house.getImageFileSet().stream().findFirst().orElse(null));
    housePost.setUser(user);
    housePost.setRegion(region);

    // 댓글 추가하기
    return housePost;
  }
}
