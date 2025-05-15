package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.entity.*;
import com.example.service.AccountService;
import com.example.service.AccountResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
 @RequestMapping
public class SocialMediaController {

    private final AccountService accountService;

    @Autowired
    public SocialMediaController (AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        try {
            Account createdAccount = accountService.register(account);
            return ResponseEntity.ok(new AccountResponse(createdAccount.getAccountId(), createdAccount.getUsername()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account loginRequest) {
        return accountService.login(loginRequest.getUsername(), loginRequest.getPassword())
                .<ResponseEntity<?>>map(account ->
                        ResponseEntity.ok(new AccountResponse(account.getAccountId(), account.getUsername())))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
    }
}
