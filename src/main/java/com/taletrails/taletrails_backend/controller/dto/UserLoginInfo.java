package com.taletrails.taletrails_backend.controller.dto;

public class UserLoginInfo {
    private Long userId;
    private String name;
    private int isQuiz;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsQuiz() {
        return isQuiz;
    }

    public void setIsQuiz(int isQuiz) {
        this.isQuiz = isQuiz;
    }
}
