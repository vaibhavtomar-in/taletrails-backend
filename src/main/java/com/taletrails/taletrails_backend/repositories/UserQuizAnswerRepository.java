// UserQuizAnswerRepository.java
package com.taletrails.taletrails_backend.repositories;

import com.taletrails.taletrails_backend.entities.UserQuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQuizAnswerRepository extends JpaRepository<UserQuizAnswer, Long> {
}
