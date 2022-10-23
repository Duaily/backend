package com.kusitms.backend.service;

import com.kusitms.backend.domain.House;
import com.kusitms.backend.domain.HousePost;
import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.HouseDto;
import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import com.kusitms.backend.repository.HouseRepository;
import com.kusitms.backend.repository.PostRepository;
import com.kusitms.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HouseService implements IHouseService {

  private final HouseRepository houseRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public Long create(String email, HouseDto request) {
    // 현재 사용자 조회
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND_EXCEPTION));

    // 빈 집 생성
    House house = House.createHouse(request, user);

    // 빈 집 저장
    House savedHouse = houseRepository.save(house);

    // 빈 집 게시글 생성
    HousePost housePost =
        HousePost.createHousePost(savedHouse, request.getTitle(), request.getImageUrls(), user);

    // 빈 집 게시글 저장
    HousePost saved = postRepository.save(housePost);

    // 빈 집 게시글 아이디 반환
    return saved.getId();
  }
}
