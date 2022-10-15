package com.kusitms.backend.repository;

import com.kusitms.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  boolean existsByNickname(String nickname);
}
