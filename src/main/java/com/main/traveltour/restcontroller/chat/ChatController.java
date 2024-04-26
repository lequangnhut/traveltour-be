package com.main.traveltour.restcontroller.chat;

import com.main.traveltour.dto.chat.UserChatResponseDto;
import com.main.traveltour.dto.chat.UserDataDto;
import com.main.traveltour.entity.ChatMessage;
import com.main.traveltour.entity.ChatNotification;
import com.main.traveltour.entity.ChatRooms;
import com.main.traveltour.entity.UserChat;
import com.main.traveltour.repository.ChatMessageRepository;
import com.main.traveltour.service.chat.ChatMessageService;
import com.main.traveltour.service.chat.ChatRoomService;
import com.main.traveltour.service.chat.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Autowired
    private UserChatService userChatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @MessageMapping("/{userId}/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.saveChatRoom(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }

    @MessageMapping("/send/notification")
    public void sendNotification(@Payload UserDataDto userDataDto) {
        List<UserChatResponseDto> userChatResponseDtos = new ArrayList<>();

        if (userDataDto.getUserId() != null) {
            Optional<UserChat> userOptional = userChatService.findByUserId(userDataDto.getUserId());

            if (userOptional.isPresent()) {
                List<ChatRooms> chatRooms = chatRoomService.findChatRoomsByRecipientId(String.valueOf(userDataDto.getUserId()));
                List<String> senderIds = chatRooms.stream()
                        .map(ChatRooms::getSenderId)
                        .collect(Collectors.toList());
                List<UserChat> userChats = userChatService.findAllByUserIds(senderIds);

                for (UserChat userChat : userChats) {
                    if (!chatRooms.isEmpty()) {
                        Integer countUnreadMessages = chatMessageRepository.countByChatIdAndRecipientIdAndStatusIs(chatRooms.get(0).getChatId(), chatRooms.get(0).getRecipientId(), false);
                        List<ChatMessage> newChatMessage = chatMessageRepository.findByChatIdOrderByTimestampDesc(chatRooms.get(0).getChatId());
                        boolean statusMessage = countUnreadMessages > 0;

                        UserChatResponseDto userChatResponseDto = UserChatResponseDto.builder()
                                .id(userChat.getId())
                                .userId(userChat.getUserId())
                                .fullName(userChat.getFullName())
                                .status(userChat.getStatus())
                                .avatar(userChat.getAvatar())
                                .lastUpdated(userChat.getLastUpdated())
                                .role(userChat.getRole())
                                .countMessageUnread(countUnreadMessages)
                                .statusMessage(statusMessage)
                                .newMessage(newChatMessage.get(0).getContent())
                                .timeMessage(newChatMessage.get(0).getTimestamp())
                                .build();

                        userChatResponseDtos.add(userChatResponseDto);
                    } else {
                        UserChatResponseDto userChatResponseDto = UserChatResponseDto.builder()
                                .id(userChat.getId())
                                .userId(userChat.getUserId())
                                .fullName(userChat.getFullName())
                                .status(userChat.getStatus())
                                .avatar(userChat.getAvatar())
                                .lastUpdated(userChat.getLastUpdated())
                                .role(userChat.getRole())
                                .countMessageUnread(0)
                                .statusMessage(true)
                                .newMessage(null)
                                .timeMessage(null)
                                .build();

                        userChatResponseDtos.add(userChatResponseDto);
                    }
                }
            }
        }
        assert userDataDto.getUserId() != null;
        messagingTemplate.convertAndSendToUser(
                userDataDto.getUserId(), "/send/notification", userChatResponseDtos
        );
    }
}
