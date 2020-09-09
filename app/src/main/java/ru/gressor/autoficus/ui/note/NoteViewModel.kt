package ru.gressor.autoficus.ui.note

import ru.gressor.autoficus.data.NotesRepository
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.model.RequestResult.*
import ru.gressor.autoficus.ui.base.BaseViewModel

class NoteViewModel : BaseViewModel<Note?, NoteViewState>() {

    private var pendingNote: Note? = null

    fun loadNote(noteId: String) {
        NotesRepository.getNoteById(noteId).observeForever {
            it?.let { result ->
                when (result) {
                    is Success<*> ->
                        viewStateLiveData.value = NoteViewState(note = result.data as? Note)
                    is Error ->
                        viewStateLiveData.value = NoteViewState(error = result.error)
                }
            }
        }
    }

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
    }
}