package com.app.service.auth;

import java.util.Map;
import com.app.dto.user.APILogin;
import com.app.dto.user.Users;

public interface AuthService {
    Map<String, Object> login(APILogin req);
    int signup(Users user);
    boolean isLoginIdAvailable(String loginId);
    boolean isNicknameAvailable(String nickname);
}
