package com.nubari.notes.dependency_injection.core

import android.app.Application
import androidx.room.Room
import com.nubari.notes.feature_note.data.data_source.NotesDatabase
import com.nubari.notes.feature_note.domain.repository.NoteRepository
import com.nubari.notes.feature_note.domain.repository.NoteRepositoryImplementation
import com.nubari.notes.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NotesDatabase {
        return Room.databaseBuilder(
            app,
            NotesDatabase::class.java,
            NotesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NotesDatabase): NoteRepository {
        return NoteRepositoryImplementation(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NotesUseCases {
        return NotesUseCases(
            getNotesUseCase = GetNotesUseCase(repository = repository),
            deleteNotesUseCase = DeleteNoteUseCase(repository = repository),
            addNote = AddNote(repository = repository),
            getNote = GetNote(repository = repository)
        )
    }
}