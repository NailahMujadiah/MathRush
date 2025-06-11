package com.example.mathrush;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

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

    @SuppressLint("Range")
    public int loginAndGetUserId(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username = ? AND password = ?",
                new String[]{username, password});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        return userId;
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

    // Tambahkan skor ke quiz_progress
    public void addOrUpdateQuizProgress(int userId, String topic, String level, int addedScore, boolean isLastQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT score FROM user_quiz_progress WHERE user_id=? AND topic=? AND level=?",
                new String[]{String.valueOf(userId), topic, level});

        if (cursor.moveToFirst()) {
            int currentScore = cursor.getInt(0);
            int newScore = currentScore + addedScore;
            String newStatus = isLastQuestion ? "completed" : "in_progress";

            ContentValues values = new ContentValues();
            values.put("score", newScore);
            values.put("status", newStatus);

            db.update("user_quiz_progress", values, "user_id=? AND topic=? AND level=?",
                    new String[]{String.valueOf(userId), topic, level});
        } else {
            String status = isLastQuestion ? "completed" : "in_progress";

            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("topic", topic);
            values.put("level", level);
            values.put("score", addedScore);
            values.put("status", status);

            db.insert("user_quiz_progress", null, values);
        }

        cursor.close();
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

        // Log seluruh data di tabel user_quiz_progress
        Cursor allCursor = db.rawQuery("SELECT * FROM user_quiz_progress", null);
        while (allCursor.moveToNext()) {
            int uid = allCursor.getInt(allCursor.getColumnIndexOrThrow("user_id"));
            String topik = allCursor.getString(allCursor.getColumnIndexOrThrow("topic"));
            Log.d("DB_CEK", "user_id: " + uid + ", topik: " + topik);
        }
        allCursor.close();

        // Query yang dipake buat dapetin data user tertentu
        Cursor cursor = db.rawQuery("SELECT * FROM user_quiz_progress WHERE user_id = ?",
                new String[]{String.valueOf(userId)});
        Log.d("DB_CEK", "userId yang dipakai untuk query: " + userId);

        return MappingHelper.mapToQuizProgressList(cursor);
    }


    public List<BadgeModel> getUserBadges(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_badges WHERE user_id = ?",
                new String[]{String.valueOf(userId)});
        return MappingHelper.mapToBadgeList(cursor);

    }
    @Nullable
    public UserModel getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userId)});

        UserModel user = null;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));

            user = new UserModel(id, username, password);
        }

        cursor.close();
        return user;
    }


    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username=?", new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    public void printAllProgressDebug() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_quiz_progress", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String topic = cursor.getString(cursor.getColumnIndexOrThrow("topic"));
                String level = cursor.getString(cursor.getColumnIndexOrThrow("level"));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                Log.d("DEBUG_PROGRESS", "ID: " + id + ", userId: " + userId + ", topic: " + topic + ", level: " + level + ", score: " + score + ", status: " + status + ", timestamp: " + timestamp);
            } while (cursor.moveToNext());
        } else {
            Log.d("DEBUG_PROGRESS", "Tabel user_quiz_progress kosong.");
        }
        cursor.close();
    }


}
