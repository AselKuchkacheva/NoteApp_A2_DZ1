package com.example.noteapp_a2.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.noteapp_a2.models.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Insert
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Update
    void update(Note note);

    @Query("SELECT * FROM note ORDER BY title ASC")
    List<Note> sortAZ();

   @Query("SELECT * FROM note ORDER BY createdAt ASC")
    List<Note> sortDate();
}
