package com.kusitms.backend.service;

import com.kusitms.backend.config.TokenProvider;
import com.kusitms.backend.dto.SignInRequest;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.util.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService {

  private final AuthenticationManagerBuilder builder;
  private final TokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final RedisClient redisClient;

  private final UserRepository userRepository;

  public String signIn(SignInRequest request) {
    UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

    Authentication authentication = builder.getObject().authenticate(authenticationToken);

    return tokenProvider.generateTokenDto(authenticationToken);
  }
}
