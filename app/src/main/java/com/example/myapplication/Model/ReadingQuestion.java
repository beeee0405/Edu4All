package com.example.myapplication.Model;

import java.util.List;

public class ReadingQuestion {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;
    private int userAnswerIndex = -1; // -1 for unanswered

    public ReadingQuestion(String questionText, List<String> options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    // Getters
    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    // Getter and Setter for user's answer
    public int getUserAnswerIndex() {
        return userAnswerIndex;
    }

    public void setUserAnswerIndex(int userAnswerIndex) {
        this.userAnswerIndex = userAnswerIndex;
    }
}
