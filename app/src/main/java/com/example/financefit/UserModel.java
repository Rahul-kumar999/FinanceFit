package com.example.financefit;

public class UserModel {
    private String name, email, mobileNo;

    public UserModel(String name, String email, String mobileNo) {
    }

    public UserModel(String name, String email) {
        this.name = name;
        this.email = email;
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
