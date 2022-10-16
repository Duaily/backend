package com.kusitms.backend.service;

import com.kusitms.backend.dto.SignInRequest;

public interface IAuthService {

  String signIn(SignInRequest request);
}
