package ru.gressor.autoficus.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gressor.autoficus.data.Repository

class MainViewModel : ViewModel() {
    private val viewStateLiveData = MutableLiveData<MainViewState>()

    init {
        viewStateLiveData.value = MainViewState(Repository.notes)
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}