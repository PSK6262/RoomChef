package com.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.app.common.CommonCode;
import com.app.dao.user.UserDAO;
import com.app.dto.user.Users;
import com.app.util.JwtProvider;

/**
 * [RoomChef 관리자 전용 API 인터셉터]
 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AdminAuthInterceptor.class);

    @Autowired(required = false)
    private UserDAO userDAO;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = JwtProvider.extractToken(request);
        if (token != null && JwtProvider.isValidToken(token)) {
            String loginId = JwtProvider.getLoginIdFromToken(token);
            if (loginId != null && userDAO != null) {
                Users user = userDAO.selectUserByLoginId(loginId);
                if (user != null && CommonCode.ROLE_ADMIN.equals(user.getRoleCode())) {
                    return true;
                }
            }
        }

        log.warn("[AdminAuthInterceptor] 관리자 권한 없는 접근 차단: URI={}", request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                "{\"status\":\"FAIL\",\"code\":\"REJ_004\",\"message\":\"관리자 권한이 필요합니다.\",\"data\":null}");
        response.getWriter().flush();
        return false;
    }
}
