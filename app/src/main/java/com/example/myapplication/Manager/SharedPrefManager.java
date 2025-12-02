package com.example.myapplication.Manager;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "Edu4AllUserStats";
    private static final String KEY_XP = "user_xp";
    private static final String KEY_LEVEL = "user_level";
    private static final String KEY_USERNAME = "user_username";
    private static final String KEY_THEME = "user_theme";
    private static final String KEY_STREAK = "user_streak";
    private static final String KEY_LESSONS_COMPLETED = "user_lessons_completed";
    private static final String KEY_ACHIEVEMENTS = "user_achievements";
    private static final String KEY_LAST_LOGIN_DATE = "user_last_login_date";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserStats(int xp, int level) {
        editor.putInt(KEY_XP, xp);
        editor.putInt(KEY_LEVEL, level);
        editor.apply();
    }

    public void saveUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public void saveTheme(int themeMode) {
        editor.putInt(KEY_THEME, themeMode);
        editor.apply();
    }

    public void saveStreak(int streak) {
        editor.putInt(KEY_STREAK, streak);
        editor.apply();
    }

    public void saveLessonsCompleted(int lessonsCompleted) {
        editor.putInt(KEY_LESSONS_COMPLETED, lessonsCompleted);
        editor.apply();
    }

    public void saveAchievements(int achievements) {
        editor.putInt(KEY_ACHIEVEMENTS, achievements);
        editor.apply();
    }

    public void saveLastLoginDate(String date) {
        editor.putString(KEY_LAST_LOGIN_DATE, date);
        editor.apply();
    }

    public void incrementLessonsCompleted() {
        int current = getLessonsCompleted();
        saveLessonsCompleted(current + 1);
    }

    public void incrementStreak() {
        int current = getStreak();
        saveStreak(current + 1);
    }

    public void incrementAchievements() {
        int current = getAchievements();
        saveAchievements(current + 1);
    }

    public int getXp() {
        return sharedPreferences.getInt(KEY_XP, 0);
    }

    public int getLevel() {
        return sharedPreferences.getInt(KEY_LEVEL, 1);
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public int getTheme() {
        return sharedPreferences.getInt(KEY_THEME, -1);
    }

    public int getStreak() {
        return sharedPreferences.getInt(KEY_STREAK, 0);
    }

    public int getLessonsCompleted() {
        return sharedPreferences.getInt(KEY_LESSONS_COMPLETED, 0);
    }

    public int getAchievements() {
        return sharedPreferences.getInt(KEY_ACHIEVEMENTS, 0);
    }

    public String getLastLoginDate() {
        return sharedPreferences.getString(KEY_LAST_LOGIN_DATE, null);
    }
}
