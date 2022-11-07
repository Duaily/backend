package com.kusitms.backend.dto;

import com.kusitms.backend.domain.Address;
import com.kusitms.backend.domain.HousePost;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HousePreviewDto {

  private Long postId;
  private String imageUrl;
  private String title;
  private String location;
  private Integer minPrice;
  private Integer maxPrice;
  private String author;

  public static HousePreviewDto toDto(HousePost housePost) {
    String origin = housePost.getHouse().getPrice();
    String start = origin.split("")[0];
    int length = origin.length();
    StringBuilder min = new StringBuilder(start);
    StringBuilder max = new StringBuilder(String.valueOf(Integer.parseInt(start) + 1));
    for (int i = 0; i < length - 1; i++) {
      min.append('0');
      max.append('0');
    }

    return HousePreviewDto.builder()
        .postId(housePost.getId())
        .imageUrl(housePost.getImageFile().getImageUrl())
        .title(housePost.getTitle())
        .location(housePost.getHouse().getAddress().getCity() + " "
            + housePost.getHouse().getAddress().getStreet())
        .minPrice(Integer.parseInt(min.toString()))
        .maxPrice(Integer.parseInt(max.toString()))
        .author(housePost.getUser().getNickname())
        .build();
  }

}
