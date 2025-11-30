package com.example.myapplication.Model;

import java.util.List;

public class ExamQuestion {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;
    private int userAnswerIndex = -1; // To store user's answer, -1 means unanswered

    public ExamQuestion(String questionText, List<String> options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public int getUserAnswerIndex() {
        return userAnswerIndex;
    }

    public void setUserAnswerIndex(int userAnswerIndex) {
        this.userAnswerIndex = userAnswerIndex;
    }
}
