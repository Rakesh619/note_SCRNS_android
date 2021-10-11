package com.note_scrns_android.DatabaseIterface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.note_scrns_android.Models.SubjectPojo;
import com.note_scrns_android.Models.SubjectNotesPojo;

import java.util.List;

@Dao
public interface Subject_Interface {

    @Query("SELECT * FROM SubjectPojo ")
    List<SubjectPojo> getAll();

    @Query("SELECT * FROM SubjectPojo WHERE subject_id = :id")
    List<SubjectPojo> getSubject(int id);

    @Insert
    void insert(SubjectPojo subject);

    @Update
    void update(SubjectPojo repos);

    @Delete
    void delete(SubjectPojo subject);

    @Delete
    void delete(SubjectPojo... subject);

}
