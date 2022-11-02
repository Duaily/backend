package com.kusitms.backend.service;

import com.kusitms.backend.dto.HouseDto;

public interface IHouseService {

  Long create(String email, HouseDto dto);
}
