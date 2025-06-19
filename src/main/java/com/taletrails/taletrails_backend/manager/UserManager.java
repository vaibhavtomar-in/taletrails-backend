package com.taletrails.taletrails_backend.manager;

import com.taletrails.taletrails_backend.manager.data.UserInfo;
import com.taletrails.taletrails_backend.manager.data.UserLogin;

public interface UserManager {
    UserLogin registerUser(UserInfo userInfo);
    UserLogin loginUser(String phoneNumber, String password);
    UserInfo getUserDetails(Long userId);
}
