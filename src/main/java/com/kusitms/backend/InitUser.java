package com.kusitms.backend;

import com.kusitms.backend.domain.Authority;
import com.kusitms.backend.domain.Region;
import com.kusitms.backend.domain.RegionPost;
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
  private final InitRegionService initRegionService;

  //@PostConstruct
  public void init() {
    initUserService.init();
    initRegionService.init();
  }

  @Component
  @RequiredArgsConstructor
  static  class InitRegionService {

    private final EntityManager entityManager;


    @Transactional
    public  void init() {
      Region region = Region.builder()
              .info("info ")
              .build();

      entityManager.persist(region);

      RegionPost regionPost = new RegionPost();
      regionPost.setRegion(region);

      entityManager.persist(regionPost);
    }
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
          .password(passwordEncoder.encode("test12345"))
          .contact("010-0000-0000")
          .nickname("Dual-test")
          .authority(Authority.ROLE_USER)
          .build();

      User broker = User.builder()
          .nickname("듀얼리중개사")
          .contact("010-1234-5678")
          .email("duaily@test.com")
          .password(passwordEncoder.encode("duaily1234"))
          .authority(Authority.ROLE_BROKER)
          .build();

      if (!entityManager.contains(user)) {
        entityManager.persist(user);
        entityManager.persist(broker);

      }

      Region region = Region.builder()
          .build();
      entityManager.persist(region);
    }
  }
}
