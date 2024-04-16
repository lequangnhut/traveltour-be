package com.main.traveltour.restcontroller.chat;

import com.main.traveltour.entity.ChatMessage;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.chat.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class ChatMessageController {


    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessage(@PathVariable String senderId,
                                                             @PathVariable String recipientId) {
        System.out.println("Sender: " + senderId + " Recipient: " + recipientId);
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{senderId}/{recipientId}/checkNoChatMessage")
    public ResponseObject checkNoChatMessage(@PathVariable String senderId,
                                             @PathVariable String recipientId) {
        Integer listChatMessage = chatMessageService.getCountChatMessageBySenderIdAndRecipientId(senderId, recipientId);

        return new ResponseObject("200", "OK", listChatMessage);

    }
}
