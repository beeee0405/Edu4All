package com.example.myapplication.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "units")
public class UnitEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private int progress;

    public UnitEntity(String title, String description, int progress) {
        this.title = title;
        this.description = description;
        this.progress = progress;
    }
}
