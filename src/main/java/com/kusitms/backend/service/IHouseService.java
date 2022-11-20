package com.kusitms.backend.service;

import com.kusitms.backend.domain.HousePost;
import com.kusitms.backend.dto.DealDto;
import com.kusitms.backend.dto.HouseDto;
import com.kusitms.backend.dto.HousePreviewDto;
import com.kusitms.backend.response.PageResponse;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface IHouseService {

  Long create(String email, HouseDto.Request dto);

  Long createDeal(DealDto.Request request, String email);

  Long modifyDeal(Long dealId, String email);

  HouseDto.Response getDetail(Long houseId);

  List<HousePreviewDto> getHousePostList(Pageable page);

  int getHousePostCount();

  PageResponse getMineList(String email, Pageable page);
}
