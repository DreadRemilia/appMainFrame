package com.example.appmainframe.Bean;

public class User {
    private String userName;
    private String userPassword;
    private String userSex;
    private String userType;
    private String userCount;
    private String msg;

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserSex() {
        return userSex;
    }

    public String getUserType() {
        return userType;
    }
}
