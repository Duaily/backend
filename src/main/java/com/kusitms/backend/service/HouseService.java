package com.kusitms.backend.service;

import com.kusitms.backend.domain.Deal;
import com.kusitms.backend.domain.House;
import com.kusitms.backend.domain.HousePost;
import com.kusitms.backend.domain.Region;
import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.DealDto;
import com.kusitms.backend.dto.HouseDto;
import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import com.kusitms.backend.repository.DealRepository;
import com.kusitms.backend.repository.HouseRepository;
import com.kusitms.backend.repository.PostRepository;
import com.kusitms.backend.repository.RegionRepository;
import com.kusitms.backend.repository.UserRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HouseService implements IHouseService {

  private final HouseRepository houseRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final RegionRepository regionRepository;
  private final DealRepository dealRepository;

  @Transactional
  public Long create(String email, HouseDto request) {
    // 현재 사용자 조회
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND_EXCEPTION));

    // 빈 집 생성
    House house = House.createHouse(request, user);

    // 빈 집 저장
    House savedHouse = houseRepository.save(house);

    // 지역 조회
    Region region = regionRepository.findById(request.getRegionId())
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND_EXCEPTION));

    // 빈 집 게시글 생성
    HousePost housePost =
        HousePost.createHousePost(savedHouse, request.getTitle(),
            request.getImageUrls(), user, region);

    // 빈 집 게시글 저장
    HousePost saved = postRepository.save(housePost);

    // 빈 집 게시글 아이디 반환
    return saved.getId();
  }

  @Transactional
  public Long createDeal(DealDto.Request request, String email) {
    // 사용자 조회
    User buyer = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND_EXCEPTION));

    // 빈 집 게시글 조회
    HousePost housePost = (HousePost) postRepository.findById(request.getPostId())
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.POST_NOT_FOUND_EXCEPTION));

    // 사용자 권한 조회 ( 작성자인지 아닌지 )
    if (housePost.getUser() == buyer) {
      throw new ApiException(ApiExceptionEnum.BUYER_INVALID_EXCEPTION);
    }

    // 거래 시작
    Deal deal = Deal.createDeal(buyer, housePost);

    // 거래 저장
    Deal saved = dealRepository.save(deal);

    return saved.getId();
  }

  @Transactional
  public Long modifyDeal(Long dealId, String email) {

    // 빈 집 게시글 판매자 조회
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND_EXCEPTION));

    // 거래 조회
    Deal deal = dealRepository.findById(dealId)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND_EXCEPTION));

    // 거래 상태 처리 확인
    if (deal.isCompleted()) {
      throw new ApiException(ApiExceptionEnum.DEAL_DONE_EXCEPTION);
    }

    User owner = deal.getHousePost().getHouse().getOwner();

    // 거래 완료 처리 권한 조회
    if (user != owner) {
      throw new ApiException(ApiExceptionEnum.SELLER_INVALID_EXCEPTION);
    }

    // 거래 완료
    deal.modifyStatus();

    // 거래 저장
    Deal saved = dealRepository.save(deal);

    return saved.getId();
  }
}
