package com.app.controller.user;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.ApiResponse;
import com.app.common.CommonCode;
import com.app.common.ResultCode;
import com.app.dto.user.Users;
import com.app.service.user.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Users>> getMyProfile(HttpServletRequest request) {
        Users loginUser = (Users) request.getAttribute(CommonCode.SESSION_LOGIN_USER);
        if (loginUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.error(ResultCode.UNAUTHORIZED));
        }

        Users profile = userService.getProfile(loginUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<String>> updateProfile(
            @RequestBody Users user, HttpServletRequest request) {
        Users loginUser = (Users) request.getAttribute(CommonCode.SESSION_LOGIN_USER);
        if (loginUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.error(ResultCode.UNAUTHORIZED));
        }

        user.setUserId(loginUser.getUserId());
        userService.updateProfile(user);
        return ResponseEntity.ok(ApiResponse.success("회원 정보가 성공적으로 수정되었습니다."));
    }
}
