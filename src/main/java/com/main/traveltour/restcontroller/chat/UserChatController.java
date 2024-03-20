package com.main.traveltour.restcontroller.chat;

import com.main.traveltour.dto.chat.UserChatDto;
import com.main.traveltour.dto.chat.UserChatResponseDto;
import com.main.traveltour.dto.chat.UserDataDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.enums.Role;
import com.main.traveltour.repository.ChatMessageRepository;
import com.main.traveltour.repository.UserChatRepository;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.AgenciesService;
import com.main.traveltour.service.chat.ChatMessageService;
import com.main.traveltour.service.chat.ChatRoomService;
import com.main.traveltour.service.chat.UserChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final UserChatService userChatService;
    private final UserChatRepository userStatusRepository;
    private final UsersService usersService;
    private final AgenciesService agenciesService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageService chatMessageService;
    @MessageMapping("/user.userConnected")
    @SendTo("/user/public")
    public void userConnected(@Payload UserChatDto userChatDto) {
        if (userChatDto.getUserId() != null) {
            Optional<UserChat> userOptional = userChatService.findByUserId(userChatDto.getUserId());

            if (userOptional.isPresent()) {
                userChatService.userConnected(Integer.valueOf(userOptional.get().getUserId()));
            } else {
                if (userChatDto.getRole().equals(Role.ROLE_CUSTOMER.name())) {
                    userChatService.insertUserStatus(userChatDto);
                } else if (
                        userChatDto.getRole().equals(Role.ROLE_AGENT_HOTEL.name()) ||
                                userChatDto.getRole().equals(Role.ROLE_AGENT_TRANSPORT.name()) ||
                                userChatDto.getRole().equals(Role.ROLE_AGENT_PLACE.name())) {
                    Agencies agencies = agenciesService.findByUserId(Integer.parseInt(userChatDto.getUserId()));

                    userChatDto.setUserId(String.valueOf(agencies.getUserId()));
                    userChatDto.setFullName(agencies.getNameAgency());

                    userChatService.insertUserStatus(userChatDto);
                }
            }
        }
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public void disconnectUser(
            @Payload UserChatDto userChatDto
    ) {
        Optional<UserChat> users = userChatService.findByUserId(userChatDto.getUserId());

        if (userChatDto.getUserId() != null) {
            if (users.isPresent()) {
                userChatService.disconnectUser(Integer.valueOf(userChatDto.getUserId()));
            } else {
                userChatService.insertUserStatus(userChatDto);
            }
        }
    }

    @MessageMapping("/chat.findStaffSupportOnline")
    @SendTo("/user/public")
    public List<UserChat> findStaffSupportOnline() {

        return userChatService.findStaffSupportOnline();
    }

    /**
     * Phương thức tìm lịch sử nhắn tin của tất cả khách hàng
     * @param userDataDto thông tin của đối tác
     * @return danh sách khách hàng
     */
    @MessageMapping("/chat.findUsersWithChatHistoryAdmin")
    @SendTo("/user/admin")
    public List<UserChatResponseDto> findUsersWithChatHistory(@Payload UserDataDto userDataDto) {
        List<UserChatResponseDto> userChatResponseDtos = new ArrayList<>();

        if (userDataDto.getUserId() != null && userDataDto.getRole() != null) {
            Optional<UserChat> userOptional = userChatService.findByUserId(userDataDto.getUserId());

            if (userOptional.isPresent()) {
                List<ChatRooms> chatRooms = chatRoomService.findChatRoomsByRecipientId(String.valueOf(userDataDto.getUserId()));
                List<String> senderIds = chatRooms.stream()
                        .map(ChatRooms::getSenderId)
                        .collect(Collectors.toList());
                List<UserChat> userChats = userChatService.findAllByUserIds(senderIds);

                for (UserChat userChat : userChats) {
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
                }
            }
        }
        return userChatResponseDtos;
    }

    @MessageMapping("/chat.updateStatusMessengerAgency")
    public void updateStatusMessengerAgency(@Payload UserDataDto userDataDto) {

        List<UserChatResponseDto> userChatResponseDtos = new ArrayList<>();

        if (userDataDto.getUserId() != null && userDataDto.getRole() != null) {
            Optional<UserChat> userOptional = userChatService.findByUserId(userDataDto.getUserId());

            if (userOptional.isPresent()) {
                List<ChatRooms> chatRooms = chatRoomService.findChatRoomsByRecipientId(String.valueOf(userDataDto.getUserId()));
                chatMessageService.updateStatusMessengerAgency(chatRooms.get(0).getChatId(), userOptional.get().getUserId());

                List<String> senderIds = chatRooms.stream()
                        .map(ChatRooms::getSenderId)
                        .collect(Collectors.toList());
                List<UserChat> userChats = userChatService.findAllByUserIds(senderIds);

                for (UserChat userChat : userChats) {
                    Integer countUnreadMessages = chatMessageRepository.countByChatIdAndRecipientIdAndStatusIs(chatRooms.get(0).getChatId(), chatRooms.get(0).getRecipientId(),false);
                    boolean statusMessage = countUnreadMessages > 0;

                    List<ChatMessage> newChatMessage = chatMessageRepository.findByChatIdOrderByTimestampDesc(chatRooms.get(0).getChatId());

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
                }
            }
            messagingTemplate.convertAndSendToUser(
                    userDataDto.getUserId(), "/queue/updateStatusMessengerAgency",
                    userChatResponseDtos
            );
        }
    }

    @MessageMapping("/chat.findUsersWithChatHistoryCustomer")
    @SendTo("/user/customer")
    public List<UserChatResponseDto> findUsersWithChatHistoryCustomer(@Payload UserDataDto userDataDto) {
        List<UserChatResponseDto> userChatResponseDtos = new ArrayList<>();
        if (userDataDto.getUserId() != null && userDataDto.getRole() != null) {
            Optional<UserChat> userOptional = userChatService.findByUserId(userDataDto.getUserId());

            if (userOptional.isPresent()) {
                List<ChatRooms> chatRooms = chatRoomService.findChatRoomsByRecipientId(String.valueOf(userDataDto.getUserId()));
                chatMessageService.updateStatusMessengerAgency(chatRooms.get(0).getChatId(), userOptional.get().getUserId());

                List<String> senderIds = chatRooms.stream()
                        .map(ChatRooms::getSenderId)
                        .collect(Collectors.toList());
                List<UserChat> userChats = userChatService.findAllByUserIds(senderIds);

                for (UserChat userChat : userChats) {
                    Integer countUnreadMessages = chatMessageRepository.countByChatIdAndRecipientIdAndStatusIs(chatRooms.get(0).getChatId(), chatRooms.get(0).getRecipientId(),false);
                    boolean statusMessage = countUnreadMessages > 0;

                    List<ChatMessage> newChatMessage = chatMessageRepository.findByChatIdOrderByTimestampDesc(chatRooms.get(0).getChatId());

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
                }
            }
        }
        return userChatResponseDtos;
    }

    @MessageMapping("/chat.findAgencyId")
    @SendTo("/user/public")
    public Optional<UserChat> findByAgencyId(@Payload("agencyId") Integer agencyId) {
        return userChatService.findByUserId(String.valueOf(agencyId));
    }

}
