package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;
    private final AccountRepository accountRepository;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.messageService = messageService;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        try {
            Account createdAccount = accountService.register(account);
            return ResponseEntity.ok(createdAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account loginRequest) {
    return accountService.login(loginRequest.getUsername(), loginRequest.getPassword())
            .<ResponseEntity<?>>map(account -> ResponseEntity.ok(account)) 
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
}

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        if (message.getMessageText().trim().isEmpty() || message.getMessageText().length() > 255) {
            return ResponseEntity.badRequest().body("Invalid message text.");
        }

        if (!accountRepository.existsById(message.getPostedBy())) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        Message created = messageService.createMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable Integer id) {
        Message message = messageService.getMessageById(id);
        if (message == null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesForUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getAllMessagesForUser(accountId);
        return ResponseEntity.ok(messages);
    }

    @PatchMapping("/messages/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer id, @RequestBody Message messageUpdate) {
        try {
            if (
                messageUpdate.getMessageText().trim().isEmpty() ||
                messageUpdate.getMessageText().length() > 255) {
                return ResponseEntity.badRequest().body("Invalid message text.");
        }

        Message updated = messageService.updateMessage(id, messageUpdate);
        if (updated == null) {
            return ResponseEntity.badRequest().build(); 
        }

        return ResponseEntity.ok(1);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer id) {
        int deletedRows = messageService.deleteMessageById(id);
        return ResponseEntity.ok(deletedRows);
    }
}
