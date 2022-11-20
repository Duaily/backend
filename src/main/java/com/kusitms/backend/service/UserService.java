package com.kusitms.backend.service;

import com.kusitms.backend.domain.User;
import com.kusitms.backend.dto.CheckSmsRequest;
import com.kusitms.backend.dto.UserDto;
import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.util.RedisClient;
import com.kusitms.backend.util.SmsClient;
import java.util.Random;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService implements IUserService {

  private final RedisClient redisClient;
  private final UserRepository userRepository;
  private final SmsClient smsClient;

  private final String prefix = "sms_verify_";

  @Transactional
  public void sendSmsCode(String contact) {
    // 랜덤 숫자 코드 생성
    Random rand = new Random();
    String numStr = String.format("%04d", rand.nextInt(10000));

    // 문자 전송
    smsClient.sendMessage(contact, numStr);

    // 레디스에 저장
    String key = prefix + contact;
    redisClient.setValue(key, numStr, 3L);
  }


  public void checkSmsCode(CheckSmsRequest request, String email) {
    // 레디스에서 정보 확인
    String key = prefix + request.getContact();
    String value = redisClient.getValue(key);

    // 유효성 검사
    if (value == null || !value.equals(request.getCode())) {
      throw new ApiException(ApiExceptionEnum.CODE_INVALID_EXCEPTION);
    }

    // 유저 조회
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND_EXCEPTION));

    // 유저 전화번호 등록
    user.setContact(request.getContact());

    userRepository.save(user);
  }

  public UserDto getUserInfo(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.USER_NOT_FOUND_EXCEPTION));

    return UserDto.builder()
        .email(user.getEmail())
        .nickname(user.getNickname())
        .contact(user.getContact()).build();
  }
}
