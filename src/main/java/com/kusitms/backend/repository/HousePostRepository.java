package com.kusitms.backend.repository;

import com.kusitms.backend.domain.HousePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HousePostRepository extends JpaRepository<HousePost, Long> {

}
