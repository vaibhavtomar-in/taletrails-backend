package com.taletrails.taletrails_backend.provider;

import com.taletrails.taletrails_backend.entities.User;
import com.taletrails.taletrails_backend.manager.data.UserInfo;
import com.taletrails.taletrails_backend.manager.data.UserQuizInfo;

import java.util.Optional;

public interface UserProvider {
    User createUser(UserInfo userInfo);
    Optional<User> getUserByPhoneNumber(String phoneNumber);
    Optional<UserInfo> getUserById(Long userId);
    void saveUserQuizAnswers(UserQuizInfo quizInfo);
    UserQuizInfo getQuizAnswersByUserId(Long userId);

}
