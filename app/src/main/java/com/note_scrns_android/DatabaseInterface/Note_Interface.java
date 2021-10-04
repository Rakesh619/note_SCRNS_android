package com.note_scrns_android.DatabaseInterface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.note_scrns_android.Models.NotesPojo;

import java.util.List;
@Dao
public interface Note_Interface {

    @Query("SELECT * FROM NotesPojo ")
    List<NotesPojo> getAll();

    /*
     * Insert the object in database
     * @param note, object to be inserted
     */
    @Insert
    void insert(NotesPojo note);

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    void update(NotesPojo repos);

    /*
     * delete the object from database
     * @param note, object to be deleted
     */
    @Delete
    void delete(NotesPojo note);

    /*
     * delete list of objects from database
     * @param note, array of objects to be deleted
     */
    @Delete
    void delete(NotesPojo... note);      // Note... is varargs, here note is an array

}
