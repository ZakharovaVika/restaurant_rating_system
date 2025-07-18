package com.example.restaurant_rating_system.enums;

public enum CuisineType {
    EUROPEAN("Европейская"),
    ITALIAN("Итальянская"),
    CHINESE("Китайская"),
    JAPANESE("Японская"),
    MEXICAN("Мексиканская"),
    INDIAN("Индийская"),
    AMERICAN("Американская"),
    OTHER("Другое"); // На всякий случай

    private final String displayName;

    CuisineType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
