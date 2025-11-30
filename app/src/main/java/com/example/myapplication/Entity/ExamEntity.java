package com.example.myapplication.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "exams")
public class ExamEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    public ExamEntity(String title) {
        this.title = title;
    }

}
