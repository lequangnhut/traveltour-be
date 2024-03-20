package com.main.traveltour.service.chat;

import com.main.traveltour.entity.UserChat;
import com.main.traveltour.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


@Repository
public class UserChatRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserChatRepositoryCustom(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertUserStatus(UserChat userChat) {
        String sql = "INSERT INTO user_chat (user_id, full_name, status, avatar, last_updated, role) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userChat.getUserId(), userChat.getFullName(), userChat.getStatus(), userChat.getAvatar(), userChat.getLastUpdated(), userChat.getRole());
    }

    public void userDisconnected(Integer userId, Timestamp lastUpdated) {
        String sql = "UPDATE user_chat SET status = 'OFFLINE', last_updated = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, lastUpdated, userId);
    }


    public void userConnected(Integer userId, Timestamp lastUpdated) {
        String sql = "UPDATE user_chat SET status = 'ONLINE', last_updated = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, lastUpdated, userId);
    }

    public void userFindConnetedUsers() {
        String sql = "SELECT * FROM user_chat WHERE status = 'ONLINE'";
        jdbcTemplate.queryForList(sql);
    }

    public Users findByIdUser(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
    }

    private static class UserRowMapper implements RowMapper<Users> {
        @Override
        public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
            Users users = Users.builder()
                    .id(rs.getInt("id"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .avatar(rs.getString("avatar"))
                    .fullName(rs.getString("full_name"))
                    .birth(rs.getDate("birth"))
                    .phone(rs.getString("phone"))
                    .address(rs.getString("address"))
                    .citizenCard(rs.getString("citizen_card"))
                    .gender(rs.getInt("gender"))
                    .dateCreated(rs.getTimestamp("date_created"))
                    .isActive(rs.getBoolean("is_active"))
                    .token(rs.getString("token"))
                    .build();
            return users;
        }
    }

}
