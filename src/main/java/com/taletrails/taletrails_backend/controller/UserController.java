package com.taletrails.taletrails_backend.controller;

import com.taletrails.taletrails_backend.controller.dto.*;
import com.taletrails.taletrails_backend.manager.UserManager;
import com.taletrails.taletrails_backend.manager.data.UserInfo;
import com.taletrails.taletrails_backend.manager.data.UserLogin;
import com.taletrails.taletrails_backend.manager.data.UserQuizInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserManager userManager;

    @PostMapping("/signup")
    public UserLoginInfo signup(HttpServletRequest request, @RequestBody NewUserRequest userRequest) {
        UserInfo userInfo = transform(userRequest);
        UserLogin userLogin = userManager.registerUser(userInfo);
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setUserId(userLogin.getUserId());
        userLoginInfo.setName(userLogin.getName());
        userLoginInfo.setIsQuiz(userLogin.getIsquizTken());
        return userLoginInfo;
    }

    @PostMapping("/login")
    public UserLoginInfo loginUser(@RequestBody UserLoginRequest loginRequest) {
        UserLogin userId = userManager.loginUser(loginRequest.getPhoneNumber(), loginRequest.getPassword());
        UserLoginInfo userId1 = new UserLoginInfo();
        userId1.setUserId(userId.getUserId());
        userId1.setName(userId.getName());
        userId1.setIsQuiz(userId.getIsquizTken());
        return  userId1;
    }

    @GetMapping("/details")
    public UserDetails getUserDetails(HttpServletRequest request, @RequestParam Long userId) {
        UserInfo userInfo = userManager.getUserDetails(userId);
        return transform(userInfo);
    }
    @PostMapping("/submit-quiz")
    public SuccessResponse submitQuiz(HttpServletRequest request, @RequestBody UserQuizResponse userQuizResponse) {
        UserQuizInfo quizInfo = transform(userQuizResponse);
        userManager.submitQuizAnswers(quizInfo);
        return new SuccessResponse("Quiz submitted successfully");
    }

    private UserQuizInfo transform(UserQuizResponse response) {
        UserQuizInfo quizInfo = new UserQuizInfo();
        quizInfo.setUserId(response.getUserId());

        List<UserQuizInfo.Answer> infoAnswers = response.getAnswers().stream().map(answer -> {
            UserQuizInfo.Answer a = new UserQuizInfo.Answer();
            a.setQuestionId(answer.getQuestionId());
            a.setQuestion(answer.getQuestion());
            a.setSelectedOption(answer.getSelectedOption());
            return a;
        }).toList();

        quizInfo.setAnswers(infoAnswers);
        return quizInfo;
    }


    private UserDetails transform(UserInfo userInfo) {
        UserDetails userDetails = new UserDetails();
        userDetails.setName(userInfo.getName());
        userDetails.setPhoneNumber(userInfo.getPhoneNumber());
        userDetails.setPincode(userInfo.getPincode());
        userDetails.setQuizTaken(userInfo.isQuizTaken());
        userDetails.setEmail(userInfo.getEmail());

        return userDetails;
    }

    private UserInfo transform(NewUserRequest request) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(request.getName());
        userInfo.setPassword(request.getPassword());
        userInfo.setPhoneNumber(request.getPhoneNumber());
        userInfo.setPincode(request.getPincode());
        userInfo.setEmail(request.getEmail());
        return userInfo;
    }
}
