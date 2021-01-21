package com.example.noteapp_a2.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.noteapp_a2.models.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract NoteDao noteDao();

}
