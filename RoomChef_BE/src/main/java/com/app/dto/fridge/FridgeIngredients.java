package com.app.dto.fridge;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class FridgeIngredients {
    private Long fridgeId;
    private Long userId;
    private String ingredientName;
    private String quantity;
    private LocalDate expireDate;
    private LocalDateTime registeredAt;

    public String getRegisteredAt() {
        if (this.registeredAt == null) return null;
        return this.registeredAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
