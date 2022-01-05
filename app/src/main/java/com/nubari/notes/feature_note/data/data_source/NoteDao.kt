package com.nubari.notes.feature_note.data.data_source

import androidx.room.*
import com.nubari.notes.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    /*
    * This is a suspend function because we are just directly
    * returning the node, and we are not wrap it around a
    * flow object
    * */
    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}