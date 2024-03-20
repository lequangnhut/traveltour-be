package com.main.traveltour.service.chat;

import com.main.traveltour.entity.ChatMessage;
import com.main.traveltour.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final ChatMessageRepositoryCustom chatMessageRepositoryCustom;

    public ChatMessage saveChatRoom(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(); // You can create your own dedicated exception
        chatMessage.setChatId(chatId);
        chatMessageRepositoryCustom.insertChatMessage(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }

    public void updateStatusMessengerAgency (String chatId, String agencyId){
        chatMessageRepositoryCustom.updateAllRecipientMessagesStatusToFalse(chatId, agencyId);
    }

}
