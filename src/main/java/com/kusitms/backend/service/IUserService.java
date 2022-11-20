package com.kusitms.backend.service;

import com.kusitms.backend.dto.CheckSmsRequest;
import com.kusitms.backend.dto.UserDto;

public interface IUserService {

  void sendSmsCode(String contact);

  void checkSmsCode(CheckSmsRequest request, String email);

  UserDto getUserInfo(String email);
}
