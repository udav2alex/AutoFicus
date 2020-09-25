package ru.gressor.autoficus.ui.splash

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.gressor.autoficus.data.NotesRepository
import ru.gressor.autoficus.data.errors.NoAuthException
import ru.gressor.autoficus.ui.base.BaseViewModel

@ExperimentalCoroutinesApi
class SplashViewModel(private val notesRepository: NotesRepository) :
    BaseViewModel<Boolean?>() {

    fun requestUser() {
        launch {
            notesRepository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }
    }
}