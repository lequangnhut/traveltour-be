package com.main.traveltour.service.chat;


import com.main.traveltour.dto.chat.UserChatDto;
import com.main.traveltour.entity.UserChat;
import com.main.traveltour.enums.Role;
import com.main.traveltour.enums.Status;
import com.main.traveltour.repository.UserChatRepository;
import com.main.traveltour.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserChatService {

    private final UserChatRepositoryCustom userStatusRepositoryCustom;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserChatRepository userChatRepository;

    public void insertUserStatus(UserChatDto userChatDto) {
        System.out.println(userChatDto.toString());
        UserChat userChat = UserChat.builder()
                .userId(String.valueOf(userChatDto.getUserId()))
                .fullName(userChatDto.getFullName())
                .avatar(userChatDto.getAvatar())
                .status(Status.ONLINE.name())
                .lastUpdated(Timestamp.valueOf(LocalDateTime.now()))
                .role(userChatDto.getRole())
                .build();
        userStatusRepositoryCustom.insertUserStatus(userChat);
    }

    public void disconnectUser(Integer id) {
        Timestamp timestamp = (Timestamp.valueOf(LocalDateTime.now()));
        userStatusRepositoryCustom.userDisconnected(id, timestamp);
    }

    public void userConnected(Integer id) {
        Timestamp timestamp = (Timestamp.valueOf(LocalDateTime.now()));
        userStatusRepositoryCustom.userConnected(id, timestamp);
    }
    public Optional<UserChat> findByUserId (String id) {
        return userChatRepository.findByUserId(id);
    }

    public List<UserChat> findStaffSupportOnline() {
        return userChatRepository.findByRoleAndStatus(Role.ROLE_STAFF_SUPPORT.name(), Status.ONLINE.name());
    }

    public List<UserChat> findAllByUserIds (List<String> userIds) {
        return userChatRepository.findAllByUserIdIn(userIds);
    }
}
