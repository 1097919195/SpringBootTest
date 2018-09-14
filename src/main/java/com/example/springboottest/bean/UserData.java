package com.example.springboottest.bean;

public class UserData {
    /**
     * 用户id
     */
    private int id;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
