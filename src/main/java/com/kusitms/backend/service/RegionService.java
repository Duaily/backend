package com.kusitms.backend.service;

import com.kusitms.backend.domain.Region;
import com.kusitms.backend.dto.RegionDto;
import com.kusitms.backend.repository.RegionRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegionService implements IRegionService {

  private final RegionRepository regionRepository;

  @Transactional
  public Long create(RegionDto request) {

    // 지역 생성
    Region region = Region.createRegion(request);

    // 지역 저장
    Region savedRegion = regionRepository.save(region);

    return savedRegion.getId();
  }
}
