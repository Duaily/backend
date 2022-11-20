package com.kusitms.backend.repository;

import com.kusitms.backend.domain.HousePost;
import com.kusitms.backend.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HousePostRepository extends JpaRepository<HousePost, Long> {

  Page<HousePost> findByUser(Pageable pageable, User user);
}
