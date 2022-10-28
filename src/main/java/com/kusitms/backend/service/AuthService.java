package com.kusitms.backend.service;

import com.kusitms.backend.config.TokenProvider;
import com.kusitms.backend.domain.Authority;
import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.AuthDto;
import com.kusitms.backend.dto.SignInRequest;
import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.util.RedisClient;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService {

  private final AuthenticationManagerBuilder managerBuilder;
  private final TokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final RedisClient redisClient;

  private final UserRepository userRepository;

  @Transactional
  public String signIn(SignInRequest request) {

    UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(
            request.getEmail(), request.getPassword()
    );

    Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
    return tokenProvider.generateTokenDto(authentication);
  }

  private void checkPassword(String request, String origin) {

    if (!passwordEncoder.matches(request, origin)) {
      throw new ApiException(ApiExceptionEnum.BAD_REQUEST_EXCEPTION);
    }
  }

  //회원가입
  @Transactional
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
  @Transactional
  public boolean checkNicknameDuplication(String nickname) {
    return userRepository.existsByNickname(nickname);
  }

  //회원 탈퇴
  @Transactional
  public void withdrawal(Long userId) {
    Optional<User> user = userRepository.findById(userId);

    userRepository.delete(user.get()); // 삭제 아니고 빈 값으로 대체하기

    //없을 경우는?

  }

}
