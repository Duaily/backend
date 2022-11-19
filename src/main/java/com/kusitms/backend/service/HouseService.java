package com.kusitms.backend.service;

import com.kusitms.backend.domain.Deal;
import com.kusitms.backend.domain.House;
import com.kusitms.backend.domain.HousePost;
import com.kusitms.backend.domain.ImageFile;
import com.kusitms.backend.domain.Region;
import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.DealDto;
import com.kusitms.backend.dto.HouseDto;
import com.kusitms.backend.dto.HouseDto.Response;
import com.kusitms.backend.dto.HousePreviewDto;
import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import com.kusitms.backend.repository.DealRepository;
import com.kusitms.backend.repository.HousePostRepository;
import com.kusitms.backend.repository.HouseRepository;
import com.kusitms.backend.repository.PostRepository;
import com.kusitms.backend.repository.RegionRepository;
import com.kusitms.backend.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HouseService implements IHouseService {

  private final HouseRepository houseRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final RegionRepository regionRepository;
  private final DealRepository dealRepository;
  private final HousePostRepository housePostRepository;

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

  @Transactional
  public HouseDto.Response getDetail(Long houseId) {
    HousePost housePost = (HousePost) postRepository.findById(houseId)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND_EXCEPTION));

    boolean isCompleted = dealRepository.existsByHousePost(housePost);

    Response houseDtoBuild = Response.builder()
        .title(housePost.getTitle())
        .location(housePost.getHouse().getAddress().getCity() + " "
            + housePost.getHouse().getAddress().getStreet())
        .imageUrls(housePost.getHouse().getImageFileSet().stream()
            .map(imageFile -> (String) imageFile.getImageUrl()).collect(Collectors.toList()))
        .size(housePost.getHouse().getSize())
        .purpose(housePost.getHouse().getPurpose())
        .author(housePost.getUser().getNickname())
        .contact(housePost.getUser().getContact())
        .isPossible(isCompleted)
        .build();

    if (housePost.getHouse().getPrice().getCategory().toString().equals("MINE")) {
      String origin = housePost.getHouse().getPrice().getCost();
      String start = origin.split("")[0];
      int length = origin.length();
      StringBuilder min = new StringBuilder(start);
      StringBuilder max = new StringBuilder(String.valueOf(Integer.parseInt(start) + 1));
      for (int i = 0; i < length - 1; i++) {
        min.append('0');
        max.append('0');
      }
      houseDtoBuild.setMinPrice(Integer.parseInt(min.toString()));
      houseDtoBuild.setMaxPrice(Integer.parseInt(max.toString()));
    } else {
      houseDtoBuild.setCost(housePost.getHouse().getPrice().getCost());
      houseDtoBuild.setDeposit(housePost.getHouse().getPrice().getDeposit());
    }
    return houseDtoBuild;
  }

  @Transactional
  public List<HousePreviewDto> getHousePostList(Pageable page) {
    Page<HousePost> list = housePostRepository.findAll(page);
    return list.getContent().stream().map(HousePreviewDto::toDto).collect(Collectors.toList());
  }

  // 빈 집 게시글의 총 개수 반환
  public int getHousePostCount() {
    return housePostRepository.findAll().size();
  }

}