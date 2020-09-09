package ru.gressor.autoficus.ui.main

import androidx.lifecycle.Observer
import ru.gressor.autoficus.data.NotesRepository
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.model.RequestResult
import ru.gressor.autoficus.data.model.RequestResult.*
import ru.gressor.autoficus.ui.base.BaseViewModel

class MainViewModel : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer<RequestResult> { result ->
        result?.let {
            @Suppress("UNCHECKED_CAST")
            when (result) {
                is Success<*> ->
                    viewStateLiveData.value = MainViewState(notes = result.data as? List<Note>)
                is Error ->
                    viewStateLiveData.value = MainViewState(error = result.error)
            }
        }
    }

    private val repositoryNotes = NotesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
        super.onCleared()
    }
}