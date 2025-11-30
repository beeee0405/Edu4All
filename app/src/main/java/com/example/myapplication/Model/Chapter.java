package com.example.myapplication.Model;

public class Chapter {
    private int id; // Added for exam ID
    private String title;
    private String lessonCount;
    private int progress;

    // Constructor for regular chapters
    public Chapter(String title, String lessonCount, int progress) {
        this.id = -1; // Default to -1 for non-exam items
        this.title = title;
        this.lessonCount = lessonCount;
        this.progress = progress;
    }

    // Constructor for exams
    public Chapter(int id, String title, String lessonCount, int progress) {
        this.id = id;
        this.title = title;
        this.lessonCount = lessonCount;
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLessonCount() {
        return lessonCount;
    }

    public int getProgress() {
        return progress;
    }
}
