package com.example.myapplication.Database;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.Entity.ExamEntity;
import com.example.myapplication.Entity.QuestionEntity;
import com.example.myapplication.Entity.UnitEntity;

public class DatabaseSeeder {

    public static void seed(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);

        // Seed Units if the table is empty
        if (db.unitDao().getAllUnits().isEmpty()) {
            Log.d("DatabaseSeeder", "Seeding Units...");
            db.unitDao().insertUnit(new UnitEntity("Unit 1: Life Stories", "5 bài học", 40));
            db.unitDao().insertUnit(new UnitEntity("Unit 2: Urbanisation", "5 bài học", 10));
            db.unitDao().insertUnit(new UnitEntity("Unit 3: The Green Movement", "5 bài học", 0));
            db.unitDao().insertUnit(new UnitEntity("Unit 4: Education System", "5 bài học", 0));
            db.unitDao().insertUnit(new UnitEntity("Unit 5: Cultural Identity", "5 bài học", 0));
        }

        // Seed Exams and Questions if the table is empty
        if (db.examDao().getAllExams().isEmpty()) {
            Log.d("DatabaseSeeder", "Seeding Exams and Questions...");
            long midTermId = db.examDao().insertExam(new ExamEntity("Đề thi giữa kì"));
            seedQuestionsForExam(db, (int) midTermId, "(Giữa kì)");

            long finalTermId = db.examDao().insertExam(new ExamEntity("Đề thi cuối kì"));
            seedQuestionsForExam(db, (int) finalTermId, "(Cuối kì)");

            long nationalExamId = db.examDao().insertExam(new ExamEntity("Đề thi THPT Quốc Gia (minh họa)"));
            seedQuestionsForExam(db, (int) nationalExamId, "(THPTQG)");
        }
    }

    // CORRECTED: Simplified and robust logic to ensure questions are created for all exams
    private static void seedQuestionsForExam(AppDatabase db, int examId, String suffix) {
        for (int i = 1; i <= 50; i++) {
            QuestionEntity question = new QuestionEntity();
            question.setExamId(examId);
            
            // Make first 5 questions real for demonstration
            if (i <= 5) {
                question.setQuestionText("Đây là câu hỏi thật số " + i + " " + suffix);
                question.setOptionA("Đáp án thật A");
                question.setOptionB("Đáp án thật B");
                question.setOptionC("Đáp án thật C");
                question.setOptionD("Đáp án thật D");
            } else { // The rest are dummy questions
                question.setQuestionText("Đây là câu hỏi mẫu số " + i + " " + suffix);
                question.setOptionA("Đáp án mẫu A");
                question.setOptionB("Đáp án mẫu B");
                question.setOptionC("Đáp án mẫu C");
                question.setOptionD("Đáp án mẫu D");
            }
            
            question.setCorrectOptionIndex(i % 4); // Just for variety
            db.questionDao().insertQuestion(question);
        }
    }
}
