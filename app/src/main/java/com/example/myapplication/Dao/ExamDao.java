package com.example.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.Entity.ExamEntity;

import java.util.List;

@Dao
public interface ExamDao {

    @Insert
    long insertExam(ExamEntity exam);

    @Query("SELECT * FROM exams")
    List<ExamEntity> getAllExams();

    // Added for search functionality
    @Query("SELECT * FROM exams WHERE title LIKE :query")
    List<ExamEntity> searchExams(String query);
}
