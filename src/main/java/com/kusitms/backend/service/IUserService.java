package com.kusitms.backend.service;

import com.kusitms.backend.dto.CheckSmsRequest;

public interface IUserService {

  void sendSmsCode(String contact);

  void checkSmsCode(CheckSmsRequest request, String email);
}
