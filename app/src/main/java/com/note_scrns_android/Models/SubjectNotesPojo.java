package com.note_scrns_android.Models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SubjectNotesPojo {
    @Embedded
    public SubjectPojo subject;

    @Relation(parentColumn = "subject_id", entityColumn = "subject_id_fk", entity = NotesPojo.class)
    public List<NotesPojo> notes;
}
