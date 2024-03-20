package com.main.traveltour.restcontroller.chat;


import com.main.traveltour.entity.*;
import com.main.traveltour.service.agent.AgenciesService;
import com.main.traveltour.service.chat.ChatMessageService;
import com.main.traveltour.service.chat.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class AgencyChatController {

    @Autowired
    UserChatService userChatService;

    @Autowired
    ChatMessageService chatMessageService;

    @Autowired
    AgenciesService agenciesService;

//    @GetMapping("/chat/findByAgencyId")
//    public ResponseObject findByAgencyId(@RequestParam("agencyId") Integer agencyId) {
//        UserChat userChat = userChatService.findByAgencyId(agencyId);
//        return new ResponseObject("200", "OK", userChat);
//    }

    @PostMapping("/chat/findUserChatById")
    public ResponseObject findUserChatById(@RequestPart("senderId") String senderId,
                                           @RequestPart("recipientId") String recipientId) {
        List<ChatMessage> chatMessages = chatMessageService.findChatMessages(senderId, recipientId);
        return new ResponseObject("200", "OK", chatMessages);
    }

    @GetMapping("/chat/findAgencyChatById")
    public ResponseObject findAgencyChatById(@RequestParam("agencyId") String agencyId) {
        Optional<UserChat> userChat = userChatService.findByUserId(agencyId);
        return new ResponseObject("200", "OK", userChat);
    }
}
