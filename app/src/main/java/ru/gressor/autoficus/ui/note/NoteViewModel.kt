package ru.gressor.autoficus.ui.note

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.gressor.autoficus.data.NotesRepository
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.ui.base.BaseViewModel

@ExperimentalCoroutinesApi
class NoteViewModel(private val notesRepository: NotesRepository) :
    BaseViewModel<NoteData>() {

    private val pendingNote: Note?
        get() = getViewStateChannel().poll()?.note

    fun saveChanges(note: Note) {
        setData(NoteData(note = note))
    }

    fun loadNote(noteId: String) {
        launch {
            try {
                setData(NoteData(note = notesRepository.getNoteById(noteId)))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    fun deleteNote() {
        launch {
            try {
                pendingNote?.let { notesRepository.deleteNote(it.id) }
                setData(NoteData(isDeleted = true))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    override fun onCleared() {
        launch {
            pendingNote?.let {
                notesRepository.saveNote(it)
                super.onCleared()
            }
        }
    }
}