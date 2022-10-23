package com.kusitms.backend.service;

import com.kusitms.backend.dto.AuthDto.Request;
import com.kusitms.backend.dto.SignInRequest;
import com.kusitms.backend.dto.TokenDto;

public interface IAuthService {

  TokenDto signIn(SignInRequest request);

  void withdrawal(Long userId);

  boolean checkNicknameDuplication(String nickname);

  void signUp(Request request);
}
