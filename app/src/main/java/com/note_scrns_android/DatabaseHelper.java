package com.note_scrns_android;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.note_scrns_android.DatabaseIterface.Note_Iterface;
import com.note_scrns_android.DatabaseIterface.Subject_Interface;

import com.note_scrns_android.Models.Notes;
import com.note_scrns_android.Models.SubjectPojo;


@Database(entities = { Notes.class, SubjectPojo.class }, version = 2)
public abstract class DatabaseHelper extends RoomDatabase {
    public abstract Note_Iterface getNoteInterface();
    public abstract Subject_Interface getSubjectInterface();
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
