package com.kusitms.backend.service;

import com.kusitms.backend.domain.Region;
import com.kusitms.backend.domain.RegionPost;
import com.kusitms.backend.dto.RegionDto;
import com.kusitms.backend.repository.PostRepository;
import com.kusitms.backend.repository.RegionRepository;
import com.kusitms.backend.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegionService implements IRegionService {

  private final RegionRepository regionRepository;
  private final PostRepository postRepository;

  @Transactional
  public Long create(RegionDto request) {

    // 지역 생성
    Region region = Region.createRegion(request);

    // 지역 저장
    Region savedRegion = regionRepository.save(region);

    // 지역 게시글 생성
    RegionPost post =
        RegionPost.createRegionPost(savedRegion, request);

    // 지역 게시글 저장
    RegionPost saved = postRepository.save(post);

    // 지역 게시글 아이디 반환
    return saved.getId();
  }
}
