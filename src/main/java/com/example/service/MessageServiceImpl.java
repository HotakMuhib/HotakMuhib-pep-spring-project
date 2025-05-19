package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message getMessageById(Integer id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(Integer id, Message message) {
        Optional<Message> existing = messageRepository.findById(id);
        if(!existing.isPresent()) {
            return null;
        }
        Message original = existing.get();
        original.setMessageText(message.getMessageText());
        return messageRepository.save(original);
    }
    @Override
    public int deleteMessageById(Integer messageId) {
        return messageRepository.deleteMessageById(messageId);
    }

    @Override
    public List<Message> getAllMessagesForUser(Integer userId) {
        return messageRepository.findByPostedBy(userId);
    }
}
