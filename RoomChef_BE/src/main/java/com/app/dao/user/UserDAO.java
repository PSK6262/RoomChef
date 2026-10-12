package com.app.dao.user;

import com.app.dto.user.Users;

public interface UserDAO {
    int insertUser(Users user);
    Users selectUserByLoginId(String loginId);
    Users selectUserByUserId(Long userId);
    int countByLoginId(String loginId);
    int countByNickname(String nickname);
    int countByEmail(String email);
    int updateUser(Users user);
}
