package com.example.mathrush;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.mathrush.MappingHelper;
import com.example.mathrush.UserModel;
import com.example.mathrush.QuizProgressModel;
import com.example.mathrush.BadgeModel;

import java.util.List;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mathrush.db";
    private static final int DATABASE_VERSION = 1;

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Table Users
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT)");

        // 2. Table Quiz Progress
        db.execSQL("CREATE TABLE user_quiz_progress (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "topic TEXT," +
                "level TEXT," +
                "score INTEGER," +
                "status TEXT," + // e.g., "completed", "incomplete"
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");

        // 3. Table User Badges
        db.execSQL("CREATE TABLE user_badges (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "badge_name TEXT," +
                "unlocked_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS user_quiz_progress");
        db.execSQL("DROP TABLE IF EXISTS user_badges");
        onCreate(db);
    }

    // ✅ USER SECTION

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username=?", new String[]{username});
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            cursor.close();
            return userId;
        }
        cursor.close();
        return -1;
    }

    // ✅ QUIZ PROGRESS SECTION

    public void insertQuizProgress(int userId, String topic, String level, int score, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("topic", topic);
        values.put("level", level);
        values.put("score", score);
        values.put("status", status);

        db.insert("user_quiz_progress", null, values);
    }

    public boolean isQuizCompleted(int userId, String topic, String level) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_quiz_progress WHERE user_id=? AND topic=? AND level=? AND status='completed'",
                new String[]{String.valueOf(userId), topic, level});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // ✅ BADGE SECTION

    public void unlockBadge(int userId, String badgeName) {
        if (!isBadgeUnlocked(userId, badgeName)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("badge_name", badgeName);
            db.insert("user_badges", null, values);
        }
    }

    public boolean isBadgeUnlocked(int userId, String badgeName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_badges WHERE user_id=? AND badge_name=?",
                new String[]{String.valueOf(userId), badgeName});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public List<QuizProgressModel> getUserProgressList(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_quiz_progress WHERE user_id = ?",
                new String[]{String.valueOf(userId)});
        return MappingHelper.mapToQuizProgressList(cursor);
    }

    public List<BadgeModel> getUserBadges(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_badges WHERE user_id = ?",
                new String[]{String.valueOf(userId)});
        return MappingHelper.mapToBadgeList(cursor);

    }
    public List<UserModel> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
        return MappingHelper.mapToUserList(cursor);
    }

    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username=?", new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }


}
