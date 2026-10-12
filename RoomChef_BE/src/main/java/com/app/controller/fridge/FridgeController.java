package com.app.controller.fridge;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.ApiResponse;
import com.app.common.CommonCode;
import com.app.common.ResultCode;
import com.app.dto.fridge.FridgeIngredients;
import com.app.dto.fridge.FridgeMatchResult;
import com.app.dto.user.Users;
import com.app.service.fridge.FridgeService;

@RestController
@RequestMapping("/api/fridge")
public class FridgeController {

    @Autowired
    private FridgeService fridgeService;

    private Long getUserId(HttpServletRequest request) {
        Users user = (Users) request.getAttribute(CommonCode.SESSION_LOGIN_USER);
        return (user != null) ? user.getUserId() : null;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FridgeIngredients>>> getFridgeIngredients(HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return ResponseEntity.status(401).body(ApiResponse.error(ResultCode.UNAUTHORIZED));

        List<FridgeIngredients> list = fridgeService.getUserIngredients(userId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addIngredient(
            @RequestBody Map<String, String> body, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return ResponseEntity.status(401).body(ApiResponse.error(ResultCode.UNAUTHORIZED));

        String name = body.get("ingredientName");
        String qty = body.get("quantity");
        String expireDateStr = body.get("expireDate");
        LocalDate expireDate = (expireDateStr != null && !expireDateStr.isBlank()) ? LocalDate.parse(expireDateStr) : null;

        fridgeService.addIngredient(userId, name, qty, expireDate);
        return ResponseEntity.ok(ApiResponse.success("재료가 냉장고에 등록되었습니다."));
    }

    @DeleteMapping("/{fridgeId}")
    public ResponseEntity<ApiResponse<String>> removeIngredient(
            @PathVariable Long fridgeId, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return ResponseEntity.status(401).body(ApiResponse.error(ResultCode.UNAUTHORIZED));

        fridgeService.removeIngredient(fridgeId, userId);
        return ResponseEntity.ok(ApiResponse.success("재료가 삭제되었습니다."));
    }

    @GetMapping("/match")
    public ResponseEntity<ApiResponse<List<FridgeMatchResult>>> matchFridgeRecipes(
            @RequestParam(defaultValue = "true") boolean allowPartial,
            HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return ResponseEntity.status(401).body(ApiResponse.error(ResultCode.UNAUTHORIZED));

        List<FridgeMatchResult> results = fridgeService.matchUserFridgeRecipes(userId, allowPartial);
        return ResponseEntity.ok(ApiResponse.success(results));
    }
}
