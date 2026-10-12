package com.app.service.user;

import com.app.dto.user.Users;

public interface UserService {
    Users getProfile(Long userId);
    int updateProfile(Users user);
}
