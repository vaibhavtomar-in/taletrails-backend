package com.taletrails.taletrails_backend.provider;

import com.taletrails.taletrails_backend.entities.User;
import com.taletrails.taletrails_backend.manager.data.UserInfo;

import java.util.Optional;

public interface UserProvider {
    User createUser(UserInfo userInfo);
    Optional<User> getUserByPhoneNumber(String phoneNumber);
    Optional<UserInfo> getUserById(Long userId);
}
