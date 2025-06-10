package com.example.mathrush;



import android.database.Cursor;
import com.example.mathrush.BadgeModel;
import com.example.mathrush.QuizProgressModel;
import com.example.mathrush.UserModel;
import java.util.ArrayList;
import java.util.List;

public class MappingHelper {

    public static UserModel mapToUser(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        return new UserModel(id, username, password);
    }

    public static BadgeModel mapToBadge(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
        String badgeName = cursor.getString(cursor.getColumnIndexOrThrow("badge_name"));
        String unlockedAt = cursor.getString(cursor.getColumnIndexOrThrow("unlocked_at"));
        return new BadgeModel(id, userId, badgeName, unlockedAt);
    }

    public static QuizProgressModel mapToQuizProgress(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
        String topic = cursor.getString(cursor.getColumnIndexOrThrow("topic"));
        String level = cursor.getString(cursor.getColumnIndexOrThrow("level"));
        int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
        String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
        String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
        return new QuizProgressModel(id, userId, topic, level, score, status, timestamp);
    }

    public static List<BadgeModel> mapToBadgeList(Cursor cursor) {
        List<BadgeModel> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(mapToBadge(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static List<QuizProgressModel> mapToQuizProgressList(Cursor cursor) {
        List<QuizProgressModel> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(mapToQuizProgress(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static List<UserModel> mapToUserList(Cursor cursor) {
        List<UserModel> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(mapToUser(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
