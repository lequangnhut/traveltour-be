package com.main.traveltour.restcontroller.chat;

import com.main.traveltour.dto.chat.UpdateMessageDto;
import com.main.traveltour.dto.chat.UserChatDto;
import com.main.traveltour.dto.chat.UserChatResponseDto;
import com.main.traveltour.dto.chat.UserDataDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.enums.Role;
import com.main.traveltour.repository.ChatMessageRepository;
import com.main.traveltour.repository.UserChatRepository;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.AgenciesService;
import com.main.traveltour.service.chat.ChatMessageRepositoryCustom;
import com.main.traveltour.service.chat.ChatMessageService;
import com.main.traveltour.service.chat.ChatRoomService;
import com.main.traveltour.service.chat.UserChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;
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
                    userChatDto.setAvatar(userChatDto.getAvatar());
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

    /**
     * Phương thức tìm lịch sử nhắn tin của tất cả khách hàng
     * @param userDataDto thông tin của đối tác
     * Trả về danh sách người dùng đã nhắn tin
     */
    @MessageMapping("{userId}/chat.findUsersChat")
    public void findUsersWithChatHistory(@Payload UserDataDto userDataDto) {
        if (userDataDto.getUserId() != null && userDataDto.getRole() != null) {
            List<UserChatResponseDto> userChatResponseDto = new ArrayList<>();

            List<ChatRooms> listChatRoom = chatRoomService.findChatRoomsByRecipientId(userDataDto.getUserId());
            for (ChatRooms room : listChatRoom) {
                try {
                    UserChat getSender = userChatService.findByUserId(room.getSenderId()).orElse(null);
                    if (getSender == null) {
                        continue;
                    }

                    ChatMessage chatNewMessage = chatMessageRepository.findFirstByChatIdOrderByTimestampDesc(room.getChatId());
                    Integer countMessageUnRead = chatMessageRepository.countByChatIdAndRecipientIdAndStatusIs(room.getChatId(), room.getRecipientId(), false);

                    UserChatResponseDto user;
                    if (chatNewMessage != null) {
                        user = UserChatResponseDto.builder()
                                .id(getSender.getId())
                                .userId(getSender.getUserId())
                                .avatar(getSender.getAvatar())
                                .fullName(getSender.getFullName())
                                .status(getSender.getStatus())
                                .lastUpdated(getSender.getLastUpdated())
                                .role(getSender.getRole())
                                .countMessageUnread(countMessageUnRead)
                                .newMessage(chatNewMessage.getContent())
                                .timeMessage(chatNewMessage.getTimestamp())
                                .statusMessage(chatNewMessage.getStatus())
                                .build();
                    } else {
                        user = UserChatResponseDto.builder()
                                .id(getSender.getId())
                                .userId(getSender.getUserId())
                                .avatar(getSender.getAvatar())
                                .fullName(getSender.getFullName())
                                .status(getSender.getStatus())
                                .lastUpdated(getSender.getLastUpdated())
                                .role(getSender.getRole())
                                .countMessageUnread(0)
                                .newMessage(null)
                                .timeMessage(null)
                                .statusMessage(true)
                                .build();
                    }
                    userChatResponseDto.add(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Sắp xếp danh sách người dùng theo thời gian tin nhắn mới nhất
            userChatResponseDto.sort((o1, o2) -> {
                Timestamp timeMessage1 = o1.getTimeMessage();
                Timestamp timeMessage2 = o2.getTimeMessage();

                if (timeMessage1 == null && timeMessage2 == null) {
                    return 0;
                } else if (timeMessage1 == null) {
                    return 1;
                } else if (timeMessage2 == null) {
                    return -1;
                } else {
                    return timeMessage2.compareTo(timeMessage1);
                }
            });

            boolean checkStaff = false;
            for(var user : userChatResponseDto){
                if(user.getUserId().equals("66")) {
                    checkStaff = true;
                    break;
                }
            }
            if(!checkStaff) {
                UserChat staff = userChatService.findByUserId(String.valueOf(66)).orElse(null);

                userChatResponseDto.add(0,UserChatResponseDto.builder()
                        .id(staff.getId())
                        .userId(staff.getUserId())
                        .avatar(staff.getAvatar())
                        .fullName(staff.getFullName())
                        .status(staff.getStatus())
                        .lastUpdated(staff.getLastUpdated())
                        .role(staff.getRole())
                        .countMessageUnread(0)
                        .newMessage(null)
                        .timeMessage(null)
                        .statusMessage(true)
                        .build());
            }
            // Gửi danh sách người dùng tới người dùng
            messagingTemplate.convertAndSendToUser(
                    userDataDto.getUserId(), "/chat/findUsersChat",
                    userChatResponseDto
            );
        }
    }


    @MessageMapping("{userId}/chat.updateStatusMessenger")
    public void updateStatusMessengerAgency(@Payload UpdateMessageDto updateMessageDto) {
        System.out.println(updateMessageDto);
        if(updateMessageDto.getSenderId() != null && updateMessageDto.getRecipientId() != null) {
            ChatRooms chatRooms = chatRoomService.findChatRoomsBySenderIdAndRecipientId(updateMessageDto.getSenderId(), updateMessageDto.getRecipientId());
            chatMessageService.updateStatusMessengerAgency(chatRooms.getChatId(), updateMessageDto.getRecipientId());
        }
    }

    @MessageMapping("/chat.findAgencyId")
    @SendTo("/user/public")
    public Optional<UserChat> findByAgencyId(@Payload("agencyId") Integer agencyId) {
        return userChatService.findByUserId(String.valueOf(agencyId));
    }



}
