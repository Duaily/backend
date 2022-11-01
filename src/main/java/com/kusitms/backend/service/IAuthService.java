package com.kusitms.backend.service;

import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.AuthDto;
import com.kusitms.backend.dto.AuthDto.Request;
import com.kusitms.backend.dto.SignInRequest;
import com.kusitms.backend.dto.TokenDto;
import com.kusitms.backend.dto.TokenRequestDto;

public interface IAuthService {

  TokenDto signIn(SignInRequest request);

  void withdrawal(Long userId);

  boolean checkNicknameDuplication(String nickname);

  TokenDto reissue(TokenRequestDto request);

  String signUp(Request request);
}
