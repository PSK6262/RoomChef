package com.app.controller.auth;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.ApiResponse;
import com.app.common.ExternalApiException;
import com.app.common.ResultCode;
import com.app.dto.user.APILogin;
import com.app.dto.user.Users;
import com.app.service.auth.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody APILogin req) {
        try {
            Map<String, Object> result = authService.login(req);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (ExternalApiException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getResultCode()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody Users user) {
        try {
            authService.signup(user);
            return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
        } catch (ExternalApiException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getResultCode()));
        }
    }

    @GetMapping("/check-login-id")
    public ResponseEntity<ApiResponse<Boolean>> checkLoginId(@RequestParam String loginId) {
        boolean available = authService.isLoginIdAvailable(loginId);
        return ResponseEntity.ok(ApiResponse.success(available));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<Boolean>> checkNickname(@RequestParam String nickname) {
        boolean available = authService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(ApiResponse.success(available));
    }
}
