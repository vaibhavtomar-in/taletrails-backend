package com.taletrails.taletrails_backend.manager.data;

public class UserLogin {
    private Long userId;
    private String name;
    private int isquizTken;

    public int getIsquizTken() {
        return isquizTken;
    }

    public void setIsquizTken(int isquizTken) {
        this.isquizTken = isquizTken;
    }

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
}
