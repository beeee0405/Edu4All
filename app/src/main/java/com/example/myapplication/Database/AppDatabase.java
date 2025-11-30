package com.example.myapplication.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.Dao.ExamDao;
import com.example.myapplication.Dao.QuestionDao;
import com.example.myapplication.Dao.UnitDao;
import com.example.myapplication.Dao.UserDao;
import com.example.myapplication.Entity.ExamEntity;
import com.example.myapplication.Entity.QuestionEntity;
import com.example.myapplication.Entity.UnitEntity;
import com.example.myapplication.Entity.UserEntity;

@Database(entities = {UserEntity.class, ExamEntity.class, QuestionEntity.class, UnitEntity.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ExamDao examDao();
    public abstract QuestionDao questionDao();
    public abstract UnitDao unitDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
