package com.example.noteapp_a2;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private SharedPreferences preferences;


    public Prefs(Context context) {
       preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public boolean isShown(){
        return preferences.getBoolean("isShown", false);
    }

    public void saveBoardStatus() {
        preferences.edit().putBoolean("isShown", true).apply();
    }

    public void deletePrefSettings (){
        preferences.edit().clear().apply();
    }

    public boolean isSortedAZ(){
        return preferences.getBoolean("isSortedAZ", false);
    }

    public void sortAZ() {
        preferences.edit().putBoolean("isSortedAZ", true).apply();
    }

    public void notSortAZ() {
        preferences.edit().putBoolean("isSortedAZ", false).apply();
    }

    public boolean isSortedDate(){
        return preferences.getBoolean("isSortedDate", false);
    }

    public void sortDate() {
        preferences.edit().putBoolean("isSortedDate", true).apply();
    }

    public void notSortDate() {
        preferences.edit().putBoolean("isSortedDate", false).apply();
    }
}
