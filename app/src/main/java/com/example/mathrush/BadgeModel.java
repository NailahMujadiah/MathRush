package com.example.mathrush;

public class BadgeModel {
    private int id;
    private int userId;
    private String badgeName;
    private String unlockedAt;

    public BadgeModel(int id, int userId, String badgeName, String unlockedAt) {
        this.id = id;
        this.userId = userId;
        this.badgeName = badgeName;
        this.unlockedAt = unlockedAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getBadgeName() { return badgeName; }
    public String getUnlockedAt() { return unlockedAt; }

    // Setters if needed
}
