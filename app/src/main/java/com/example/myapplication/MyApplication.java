package com.example.myapplication;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.Database.DatabaseSeeder;
import com.example.myapplication.Manager.SharedPrefManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Apply the saved theme
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        int theme = sharedPrefManager.getTheme();
        if (theme != -1) {
            AppCompatDelegate.setDefaultNightMode(theme);
        }

        // Seed the database with initial data
        DatabaseSeeder.seed(this);
    }
}
