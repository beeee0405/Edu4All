package com.example.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Entity.UnitEntity;

import java.util.List;

@Dao
public interface UnitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUnit(UnitEntity unit);

    @Query("SELECT * FROM units")
    List<UnitEntity> getAllUnits();

    // Added for search functionality
    @Query("SELECT * FROM units WHERE title LIKE :query")
    List<UnitEntity> searchUnits(String query);
}
