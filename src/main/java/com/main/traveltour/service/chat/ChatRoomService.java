package com.main.traveltour.service.chat;

import com.main.traveltour.entity.ChatRooms;
import com.main.traveltour.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomRepositoryCustom chatRoomRepositoryCustom;

    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRooms::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }

                    return  Optional.empty();
                });
    }

    private String createChatId(String senderId, String recipientId) {
        List<String> ids = Arrays.asList(senderId, recipientId);
        Collections.sort(ids);

        String chatId = String.join("_", ids);

        ChatRooms senderRecipient = ChatRooms.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRooms recipientSender = ChatRooms.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepositoryCustom.insertChatRoom(senderRecipient);
        chatRoomRepositoryCustom.insertChatRoom(recipientSender);

        return chatId;
    }


    /**
     * Tìm kiếm các phòng chat theo người nhận
     * @param recipientId người nhận
     * @return danh sách các phòng chat
     */
    public List<ChatRooms> findChatRoomsByRecipientId(String recipientId) {
        return chatRoomRepository.findByRecipientId(recipientId);
    }

    public List<ChatRooms> findChatRoomsBySenderId(String senderId) {
        return chatRoomRepository.findBySenderId(senderId);
    }

    public ChatRooms findChatRoomsBySenderIdAndRecipientId(String senderId, String recipientId) {
        return chatRoomRepository.findFirstBySenderIdAndRecipientId(senderId, recipientId).orElse(null);
    };
}
