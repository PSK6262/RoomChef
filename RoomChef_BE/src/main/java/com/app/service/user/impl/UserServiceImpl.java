package com.app.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.user.UserDAO;
import com.app.dto.user.Users;
import com.app.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public Users getProfile(Long userId) {
        return userDAO.selectUserByUserId(userId);
    }

    @Override
    public int updateProfile(Users user) {
        return userDAO.updateUser(user);
    }
}
