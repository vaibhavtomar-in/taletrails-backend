package com.taletrails.taletrails_backend.manager.impl;

import com.taletrails.taletrails_backend.entities.User;
import com.taletrails.taletrails_backend.exception.LogitracError;
import com.taletrails.taletrails_backend.exception.LogitrackException;
import com.taletrails.taletrails_backend.manager.UserManager;
import com.taletrails.taletrails_backend.manager.data.UserInfo;
import com.taletrails.taletrails_backend.manager.data.UserLogin;
import com.taletrails.taletrails_backend.manager.data.UserQuizInfo;
import com.taletrails.taletrails_backend.provider.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserManagerImpl implements UserManager {

    @Autowired
    UserProvider userProvider;


    public UserLogin registerUser(UserInfo userInfo) {
        User user = userProvider.createUser(userInfo);
        UserLogin user1 = new UserLogin();
        user1.setUserId(user.getId());
        user1.setName(user.getName());
        user1.setIsquizTken(user.getIsQuizTaken());
        return user1;
    }

    @Override
    public UserLogin loginUser(String phoneNumber, String password) {
        Optional<User> existingUser = userProvider.getUserByPhoneNumber(phoneNumber);

        if (existingUser.isEmpty()) {
            throw new LogitrackException(LogitracError.ACCOUNT_DOES_NOT_EXIST);
        }

        User user = existingUser.get();
        if (!user.getPassword().equals(password)) {
            throw new LogitrackException(LogitracError.INVALID_PASSWORD);
        }
        UserLogin user1 = new UserLogin();
        user1.setUserId(user.getId());
        user1.setName(user.getName());
        user1.setIsquizTken(user.getIsQuizTaken());

        return user1;
    }

    @Override
    public UserInfo getUserDetails(Long userId) {
        Optional<UserInfo> userInfo = userProvider.getUserById(userId);

        if (userInfo.isEmpty()) {
            throw new LogitrackException(LogitracError.ACCOUNT_DOES_NOT_EXIST);
        }

        return userInfo.get();
    }

    @Override
    public void submitQuizAnswers(UserQuizInfo quizInfo) {
        Optional<UserInfo> optionalUser = userProvider.getUserById(quizInfo.getUserId());

        if (optionalUser.isEmpty()) {
            throw new LogitrackException(LogitracError.ACCOUNT_DOES_NOT_EXIST);
        }

        userProvider.saveUserQuizAnswers(quizInfo);
    }


}
