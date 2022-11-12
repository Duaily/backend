package com.kusitms.backend.service;

import com.kusitms.backend.domain.RegionPost;
import com.kusitms.backend.dto.RegionDto;

public interface IRegionService {

  Long create(RegionDto request);

  RegionPost getDetail(java.lang.Long regionId);
}
