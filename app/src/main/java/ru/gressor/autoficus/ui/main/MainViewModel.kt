package ru.gressor.autoficus.ui.main

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.gressor.autoficus.data.NotesRepository
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.model.RequestResult.*
import ru.gressor.autoficus.ui.base.BaseViewModel

@ExperimentalCoroutinesApi
class MainViewModel(notesRepository: NotesRepository) :
    BaseViewModel<List<Note>?>() {

    private val notesChannel = notesRepository.getNotes()

    init {
        launch {
            notesChannel.consumeEach {
                @Suppress("UNCHECKED_CAST")
                when (it) {
                    is Success<*> -> setData(it.data as? List<Note>)
                    is Error -> setError(it.error)
                }
            }
        }
    }

    override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }
}