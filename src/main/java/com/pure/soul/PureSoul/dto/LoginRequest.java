package com.pure.soul.PureSoul.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String login; // can be username or email
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
