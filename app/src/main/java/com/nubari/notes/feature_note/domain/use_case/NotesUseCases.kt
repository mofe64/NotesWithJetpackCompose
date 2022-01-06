package com.nubari.notes.feature_note.domain.use_case

data class NotesUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNotesUseCase: DeleteNoteUseCase,
    val addNote: AddNote
)
