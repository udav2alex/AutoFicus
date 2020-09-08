package ru.gressor.autoficus.ui.note

import androidx.lifecycle.ViewModel
import ru.gressor.autoficus.data.NotesRepository
import ru.gressor.autoficus.data.entity.Note

class NoteViewModel: ViewModel() {

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
    }
}