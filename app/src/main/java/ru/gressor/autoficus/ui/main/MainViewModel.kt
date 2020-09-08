package ru.gressor.autoficus.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gressor.autoficus.data.NotesRepository

class MainViewModel : ViewModel() {
    private val viewStateLiveData = MutableLiveData<MainViewState>()

    init {
        NotesRepository.getNotes().observeForever { notes ->
            notes?.let { it ->
                viewStateLiveData.value =
                    viewStateLiveData.value?.copy(notes = it) ?: MainViewState(it)
            }
        }
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}