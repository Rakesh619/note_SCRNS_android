package com.note_scrns_android.DatabaseIterface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.note_scrns_android.Models.Notes;

import java.util.List;
@Dao
public interface Note_Iterface {

    @Query("SELECT * FROM Notes ")
    List<Notes> getAll();

    @Insert
    void insert(Notes note);

    @Update
    void update(Notes repos);

    @Delete
    void delete(Notes note);


    @Delete
    void delete(Notes... note);

}
