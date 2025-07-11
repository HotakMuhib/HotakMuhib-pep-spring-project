package com.example.service;

import com.example.entity.Account;

public class AccountResponse {
    
    private Integer accountId;
    private String username;

    public AccountResponse(Integer accountId, String username) {
        this.accountId = accountId;
        this.username = username;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }
}
