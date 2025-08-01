package com.taletrails.taletrails_backend.repositories;

import com.taletrails.taletrails_backend.entities.User;
import com.taletrails.taletrails_backend.entities.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalkRepository extends JpaRepository<Walk, Long> {
    // You can add custom methods if needed, e.g. findByUserId
    List<Walk> findByUser(User user);
    List<Walk> findByUser_IdOrderByIdDesc(Long userId);
    List<Walk> findByUser_IdAndStatus(Long userId, String status);
    List<Walk> findByUser_IdAndStatusNot(Long userId, String status);


}
