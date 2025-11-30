package com.example.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Entity.QuestionEntity;

import java.util.List;

@Dao
public interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(QuestionEntity question);

    @Query("SELECT * FROM questions WHERE examId = :examId")
    List<QuestionEntity> getQuestionsByExamId(int examId);
}
