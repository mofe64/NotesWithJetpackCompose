package com.nubari.notes.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nubari.notes.feature_note.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NotesDatabase: RoomDatabase() {
    abstract val noteDao: NoteDao
}