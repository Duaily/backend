package com.kusitms.backend.service;

import com.kusitms.backend.config.TokenProvider;
import com.kusitms.backend.domain.Authority;
import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.AuthDto;
import com.kusitms.backend.dto.SignInRequest;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.util.RedisClient;
import java.util.Optional;
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

    return tokenProvider.generateTokenDto(authentication);
  }

  //회원가입
  public void signUp(AuthDto.Request request) {

    User user = User.builder()
        .nickname(request.getNickname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .contact(request.getContact())
        .authority(Authority.ROLE_USER)
        .build();
    userRepository.save(user);
  }

  //닉네임 중복 체크
  public boolean checkNicknameDuplication(String nickname) {
    return userRepository.existsByNickname(nickname);
  }

  //회원 탈퇴
  public void withdrawal(Long userId) {
    Optional<User> user = userRepository.findById(userId);

    userRepository.delete(user.get());

    //없을 경우는?

  }

}
