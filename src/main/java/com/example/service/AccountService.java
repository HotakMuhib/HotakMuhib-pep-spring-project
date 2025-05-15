package com.example.service;

import com.example.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.*;

import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private String username;
    private String password;

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService (AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register (Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }

        if (account.getPassword() == null || account.getPassword().length() <= 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }

        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("Username already exists");
        }

        return accountRepository.save(account);
    }

    public Optional<Account> login(String username, String password) {
        return accountRepository.findByUsername(username).filter(account -> account.getPassword().equals(password));
    }
    }