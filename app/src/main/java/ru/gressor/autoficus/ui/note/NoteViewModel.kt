package ru.gressor.autoficus.ui.note

import android.util.Log
import ru.gressor.autoficus.data.NotesRepository
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.model.RequestResult.*
import ru.gressor.autoficus.ui.base.BaseViewModel

class NoteViewModel(private val notesRepository: NotesRepository) :
    BaseViewModel<NoteViewState.Data, NoteViewState>() {

    private val pendingNote: Note?
        get() = viewStateLiveData.value?.data?.note

    fun loadNote(noteId: String) {
        notesRepository.getNoteById(noteId).observeForever {
            it?.let { result ->
                when (result) {
                    is Success<*> -> viewStateLiveData.value =
                        NoteViewState(NoteViewState.Data(note = result.data as? Note))
                    is Error -> viewStateLiveData.value =
                        NoteViewState(error = result.error)
                }
            }
        }
    }

    fun deleteNote() {
        pendingNote?.let {
            Log.d("FireDbDataProvider___", "RequestResult???")
            notesRepository.deleteNote(it.id).observeForever { requestResult ->
                Log.d("FireDbDataProvider___", "RequestResult")
                requestResult ?: return@observeForever
                when (requestResult) {
                    is Success<*> -> viewStateLiveData.value =
                        NoteViewState(NoteViewState.Data(isDeleted = true))
                    is Error -> viewStateLiveData.value =
                        NoteViewState(error = requestResult.error)
                }
            }
        }
    }

    fun saveChanges(note: Note) {
        viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = note))
    }

    override fun onCleared() {
        pendingNote?.let {
            notesRepository.saveNote(it)
        }
    }
}