package com.kusitms.backend.service;

import com.kusitms.backend.config.CustomUserDetailService;
import com.kusitms.backend.config.TokenProvider;
import com.kusitms.backend.domain.Authority;
import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.AuthDto;
import com.kusitms.backend.dto.SignInRequest;
import com.kusitms.backend.dto.TokenDto;
import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.util.RedisClient;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService {

  private final CustomUserDetailService customUserDetailService;
  private final TokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final RedisClient redisClient;

  private final UserRepository userRepository;

  public TokenDto signIn(SignInRequest request) {

    User user = (User) customUserDetailService.loadUserByUsername(request.getEmail());
    checkPassword(request.getPassword(), user.getPassword());

    String accessToken = tokenProvider.createAccessToken(user.getEmail());
    String refreshToken = tokenProvider.createRefreshToken(user);
    return TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
  }

  private void checkPassword(String request, String origin) {

    if (!passwordEncoder.matches(request, origin)) {
      throw new ApiException(ApiExceptionEnum.BAD_REQUEST_EXCEPTION);
    }
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
