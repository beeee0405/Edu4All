package com.example.myapplication.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "questions",
        foreignKeys = @ForeignKey(entity = ExamEntity.class,
                                  parentColumns = "id",
                                  childColumns = "examId",
                                  onDelete = ForeignKey.CASCADE))
public class QuestionEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(index = true)
    private int examId;

    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int correctOptionIndex; // 0 for A, 1 for B, etc.

}
