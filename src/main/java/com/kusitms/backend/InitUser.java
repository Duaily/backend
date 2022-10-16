package com.kusitms.backend;

import com.kusitms.backend.domain.User;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitUser {

  private final InitUserService initUserService;

  @PostConstruct
  public void init() {
    initUserService.init();
  }

  @Component
  @RequiredArgsConstructor
  static class InitUserService {
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void init() {
      User user = User.builder()
          .email("test@test.com")
          .password(passwordEncoder.encode("test12"))
          .contact("010-0000-0000")
          .nickname("Dual")
          .build();
      entityManager.persist(user);
    }
  }
}
