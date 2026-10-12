package com.app.service.auth.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.common.ExternalApiException;
import com.app.common.ResultCode;
import com.app.dao.user.UserDAO;
import com.app.dto.user.APILogin;
import com.app.dto.user.Users;
import com.app.service.auth.AuthService;
import com.app.util.JwtProvider;
import com.app.util.SHA256Encryptor;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public Map<String, Object> login(APILogin req) {
        if (req == null || req.getLoginId() == null || req.getPassword() == null) {
            throw new ExternalApiException(ResultCode.INVALID_INPUT);
        }

        Users user = userDAO.selectUserByLoginId(req.getLoginId());
        if (user == null) {
            throw new ExternalApiException(ResultCode.USER_NOT_FOUND);
        }

        String encryptedInputPassword = SHA256Encryptor.encrypt(req.getPassword(), req.getLoginId());
        if (!user.getPassword().equals(encryptedInputPassword) && !user.getPassword().equals(req.getPassword())) {
            throw new ExternalApiException(ResultCode.INVALID_PASSWORD);
        }

        String token = JwtProvider.createAccessToken(user.getLoginId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getUserId());
        result.put("loginId", user.getLoginId());
        result.put("nickname", user.getNickname());
        result.put("email", user.getEmail());
        result.put("roleCode", user.getRoleCode());

        return result;
    }

    @Override
    public int signup(Users user) {
        if (!isLoginIdAvailable(user.getLoginId())) {
            throw new ExternalApiException(ResultCode.DUPLICATE_LOGIN_ID);
        }
        if (!isNicknameAvailable(user.getNickname())) {
            throw new ExternalApiException(ResultCode.DUPLICATE_NICKNAME);
        }

        String encryptedPassword = SHA256Encryptor.encrypt(user.getPassword(), user.getLoginId());
        user.setPassword(encryptedPassword);
        return userDAO.insertUser(user);
    }

    @Override
    public boolean isLoginIdAvailable(String loginId) {
        return userDAO.countByLoginId(loginId) == 0;
    }

    @Override
    public boolean isNicknameAvailable(String nickname) {
        return userDAO.countByNickname(nickname) == 0;
    }
}
