package com.note_scrns_android.DatabaseInterface;

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
    List<SubjectNotesPojo> loadSubjectsWithNotes();

    @Query("SELECT * FROM SubjectPojo ")
    List<SubjectPojo> getAll();

    @Query("SELECT * FROM SubjectPojo WHERE subject_id = :id")
    List<SubjectPojo> getSubject(int id);


    /*
     * Insert the object in database
     * @param note, object to be inserted
     */
    @Insert
    void insert(SubjectPojo subject);

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    void update(SubjectPojo repos);

    /*
     * delete the object from database
     * @param note, object to be deleted
     */
    @Delete
    void delete(SubjectPojo subject);

    /*
     * delete list of objects from database
     * @param note, array of objects to be deleted
     */
    @Delete
    void delete(SubjectPojo... subject);      // Note... is varargs, here note is an array

}
