package com.note_scrns_android;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.note_scrns_android.DatabaseInterface.Note_Interface;
import com.note_scrns_android.DatabaseInterface.Subject_Interface;

import com.note_scrns_android.Models.NotesPojo;
import com.note_scrns_android.Models.SubjectPojo;


@Database(entities = { NotesPojo.class, SubjectPojo.class }, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {
    public abstract Note_Interface getNoteDao();
    public abstract Subject_Interface getSubjectDao();
    private static DatabaseHelper noteDB;
    public static DatabaseHelper getInstance(Context context) {
        if (null == noteDB) {
            noteDB = buildDatabaseInstance(context);
        }
        return noteDB;
    }

    private static DatabaseHelper buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                DatabaseHelper.class,
                "NotesDB")
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        noteDB = null;
    }

}
