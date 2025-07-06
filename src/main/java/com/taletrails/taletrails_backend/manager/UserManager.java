package com.taletrails.taletrails_backend.manager;

import com.taletrails.taletrails_backend.manager.data.UserInfo;
import com.taletrails.taletrails_backend.manager.data.UserLogin;
import com.taletrails.taletrails_backend.manager.data.UserQuizInfo;

public interface UserManager {
    UserLogin registerUser(UserInfo userInfo);
    UserLogin loginUser(String phoneNumber, String password);
    UserInfo getUserDetails(Long userId);
    void submitQuizAnswers(UserQuizInfo quizInfo);
    UserQuizInfo getQuizAnswers(Long userId);

}
