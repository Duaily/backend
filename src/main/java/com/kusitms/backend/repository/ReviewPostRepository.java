package com.kusitms.backend.repository;

import com.kusitms.backend.domain.ReviewPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewPostRepository extends JpaRepository<ReviewPost, Long> {

}
