// UserQuizAnswerRepository.java
package com.taletrails.taletrails_backend.repositories;

import com.taletrails.taletrails_backend.entities.UserQuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQuizAnswerRepository extends JpaRepository<UserQuizAnswer, Long> {
    List<UserQuizAnswer> findByUser_Id(Long userId);

}
