package com.nubari.notes.feature_note.domain.use_case

import com.nubari.notes.feature_note.domain.model.Note
import com.nubari.notes.feature_note.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}