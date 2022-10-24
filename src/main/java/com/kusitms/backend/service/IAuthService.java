package com.kusitms.backend.service;

import com.kusitms.backend.dto.AuthDto.Request;
import com.kusitms.backend.dto.SignInRequest;

public interface IAuthService {

  String signIn(SignInRequest request);

  void withdrawal(Long userId);

  boolean checkNicknameDuplication(String nickname);

  void signUp(Request request);
}
