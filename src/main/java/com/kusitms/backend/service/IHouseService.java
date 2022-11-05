package com.kusitms.backend.service;

import com.kusitms.backend.dto.DealDto;
import com.kusitms.backend.dto.HouseDto;

public interface IHouseService {

  Long create(String email, HouseDto dto);

  Long createDeal(DealDto.Request request, String email);

  Long modifyDeal(Long dealId, String email);
}
