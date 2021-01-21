package com.example.noteapp_a2;

import android.app.Application;

import androidx.room.Room;

import com.example.noteapp_a2.room.AppDataBase;

public class App extends Application {

    private static AppDataBase appDataBase;
    private static Prefs prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        appDataBase = Room.databaseBuilder(this, AppDataBase.class,
                "database").allowMainThreadQueries().build();
        prefs = new Prefs(this);
    }

    public static AppDataBase getAppDataBase() {
        return appDataBase;
    }

    public static Prefs getPrefs() {
        return prefs;
    }
}

