package com.example.mynotepade.entity;

public class UsersInfo {

    public String phone;
    public String password;

    public UsersInfo() {

    }

    public UsersInfo(String phone) {
        this.phone = phone;
    }

    public UsersInfo(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
