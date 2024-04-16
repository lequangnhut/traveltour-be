package com.main.traveltour.restcontroller.chat;

import com.main.traveltour.dto.chat.UserChatNotificationDto;
import com.main.traveltour.entity.ChatMessage;
import com.main.traveltour.entity.ChatNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class SendNotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/{userId}/send-notification-order")
    public void processMessage(@Payload UserChatNotificationDto user) {
        messagingTemplate.convertAndSendToUser(
                String.valueOf(user.getRecipientId()), "/send/send-notification-order", user.getContent()
        );
    }
}
