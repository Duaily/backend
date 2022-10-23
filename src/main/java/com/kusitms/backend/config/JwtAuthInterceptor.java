package com.kusitms.backend.config;

import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import com.kusitms.backend.repository.UserRepository;
import com.kusitms.backend.util.RedisClient;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

  private final TokenProvider tokenProvider;
  private final UserRepository userRepository;
  private final RedisClient redisClient;

  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response, Object handler) {
    String token = tokenProvider.resolveToken(request);

    if (!tokenProvider.validateToken(token)) {
      throw new ApiException(ApiExceptionEnum.ACCESS_DENIED_EXCEPTION);
    }

    // refresh token 확인

    return true;
  }
}
