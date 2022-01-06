package com.nubari.notes.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nubari.notes.feature_note.domain.model.Note
import com.nubari.notes.feature_note.domain.use_case.NotesUseCases
import com.nubari.notes.feature_note.domain.util.NoteOrder
import com.nubari.notes.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases
) : ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state
    private var recentlyDeletedNote: Note? = null
    private var getNoteJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                /*
                * we want to check if current note order is equal to the one user is asking
                * us to change it to and the note order type (ie. asc or dsc) is the same
                * we want to return
                * we use the class to compare the note order because we don't want to use
                * referential equality which will always be false, because the reference in the
                * our state is different from the event ref because the event ref was just created
                * when the user attempted to modify the order.
                *
                * */
                if (
                    state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.Delete -> {
                viewModelScope.launch {
                    notesUseCases.deleteNotesUseCase(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    notesUseCases.addNote(
                        recentlyDeletedNote ?: return@launch
                    )
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                /*
                * Since our state is a class with multiple
                * members, we don't directly reassign it
                * rather we copy the values and change the
                * specific value we want
                * */
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    /*
    * whenever we call this function we
    * cancel the old coroutine/flow that is already observing the data
    * We need to cancel the old job and run a new one because we've changed the note order
    * so we should fetch from database and apply the new order
    * */
    private fun getNotes(noteOrder: NoteOrder) {
        getNoteJob?.cancel()
        getNoteJob = notesUseCases.getNotesUseCase(noteOrder = noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}
