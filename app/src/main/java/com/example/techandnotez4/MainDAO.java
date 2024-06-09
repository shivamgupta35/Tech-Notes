package com.example.techandnotez4;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MainDAO {
    @Insert(onConflict = REPLACE)
    void insert(NotesModel notesModel);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<NotesModel> getAll();

    @Query("UPDATE notes SET title = :title, notes = :notes, date = :date WHERE ID = :id")
    void update(int id, String title, String notes, String date);

    @Delete
    void delete(NotesModel notesModel);

    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, Boolean pin);
}
