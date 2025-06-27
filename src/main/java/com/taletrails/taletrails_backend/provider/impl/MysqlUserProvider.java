package com.taletrails.taletrails_backend.provider.impl;

import com.taletrails.taletrails_backend.entities.User;
import com.taletrails.taletrails_backend.entities.UserQuizAnswer;
import com.taletrails.taletrails_backend.exception.LogitracError;
import com.taletrails.taletrails_backend.exception.LogitrackException;
import com.taletrails.taletrails_backend.manager.data.UserInfo;
import com.taletrails.taletrails_backend.manager.data.UserQuizInfo;
import com.taletrails.taletrails_backend.provider.UserProvider;
import com.taletrails.taletrails_backend.repositories.UserQuizAnswerRepository;
import com.taletrails.taletrails_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MysqlUserProvider implements UserProvider {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserQuizAnswerRepository quizAnswerRepository;

    @Autowired
    public MysqlUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserInfo userInfo) {
        Optional<User> existingUser = userRepository.findByPhoneNumber(userInfo.getPhoneNumber());

        if (existingUser.isPresent()) {
            throw new LogitrackException(LogitracError.ACCOUNT_ALREADY_EXISTS);
        }
        User user = transform(userInfo);
        userRepository.save(user);
        user = userRepository.findByPhoneNumber(user.getPhoneNumber()).get();
        return user;
    }

    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<UserInfo> getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(this::transform);
    }

    @Override
    public void saveUserQuizAnswers(UserQuizInfo quizInfo) {
        for (UserQuizInfo.Answer a : quizInfo.getAnswers()) {
            UserQuizAnswer entity = new UserQuizAnswer();
            entity.setUser(userRepository.findById(quizInfo.getUserId()).get());
            entity.setQuestionId(a.getQuestionId());
            entity.setQuestion(a.getQuestion());
            entity.setSelectedOption(a.getSelectedOption());
            quizAnswerRepository.save(entity);
        }

        User user = userRepository.findById(quizInfo.getUserId()).get();
        user.setIsQuizTaken(1);
        userRepository.save(user);
    }

    private UserInfo transform(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(user.getName());
        userInfo.setPhoneNumber(user.getPhoneNumber());
        userInfo.setPincode(user.getPincode());
        userInfo.setEmail(user.getEmail());
        return userInfo;
    }

    private User transform(UserInfo userInfo) {
        User user = new User();
        user.setName(userInfo.getName());
        user.setPhoneNumber(userInfo.getPhoneNumber());
        user.setPassword(userInfo.getPassword()); // Encrypt password
        user.setPincode(userInfo.getPincode());
        user.setEmail(userInfo.getEmail());
        return user;
    }
}
