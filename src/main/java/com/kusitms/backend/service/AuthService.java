package com.kusitms.backend.service;

import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.AuthDto;
import com.kusitms.backend.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;

  //회원가입
  public AuthDto.Response signUp(AuthDto.Request request) {
    User newUser = new User();
    BeanUtils.copyProperties(request, newUser);

    User savedUser = userRepository.save(newUser);

    AuthDto.Response response = new AuthDto.Response();
    BeanUtils.copyProperties(savedUser, response);

    return response;
  }

  //닉네임 중복 체크
  public boolean checkNicknameDuplication(String nickname) {
    return userRepository.existsByNickname(nickname);
  }

  //회원 탈퇴
  public void withdrawal(int userId) {
    Optional<User> user = userRepository.findById(userId);

    userRepository.delete(user.get());

    //없을 경우는?

  }


}
