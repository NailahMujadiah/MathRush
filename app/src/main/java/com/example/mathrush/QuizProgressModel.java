package com.example.mathrush;

public class QuizProgressModel {
    private int id;
    private int userId;

    public String getTitle() {
        return title;
    }

    private String title;
    private String topic;
    private String level;
    private int score;
    private String status;
    private String timestamp;

    public QuizProgressModel(int id, int userId, String topic, String level, int score, String status, String timestamp) {
        this.id = id;
        this.userId = userId;
        this.topic = topic;
        this.level = level;
        this.score = score;
        this.status = status;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTopic() { return topic; }
    public String getLevel() { return level; }
    public int getScore() { return score; }
    public String getStatus() { return status; }
    public String getTimestamp() { return timestamp; }

    // Setters if needed
}
