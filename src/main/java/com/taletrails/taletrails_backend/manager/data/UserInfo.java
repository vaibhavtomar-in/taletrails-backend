package com.taletrails.taletrails_backend.manager.data;

public class UserInfo {
    private Long id;
    private String name;
    private String phoneNumber;
    private String password;
    private String pincode;
    private String email;


    // Constructor
    public UserInfo(){

    }
    public UserInfo(Long id, String name, String phoneNumber, String password, String pincode, String email) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.pincode = pincode;
        this.email = email;

    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
