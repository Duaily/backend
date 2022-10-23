package com.kusitms.backend.domain;

import java.util.List;
import javax.persistence.DiscriminatorValue;
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
      User user) {
    HousePost housePost = new HousePost();
    housePost.setHouse(house);
    housePost.setTitle(title);
    housePost.setImageFile(house.getImageFileSet().stream().findFirst().orElse(null));
    housePost.setUser(user);
    return housePost;
  }
}
