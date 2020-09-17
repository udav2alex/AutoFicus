package ru.gressor.autoficus.ui.splash

import ru.gressor.autoficus.data.NotesRepository
import ru.gressor.autoficus.data.errors.NoAuthException
import ru.gressor.autoficus.ui.base.BaseViewModel

class SplashViewModel(private val notesRepository: NotesRepository) :
    BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        notesRepository.getCurrentUser().observeForever {user ->
            viewStateLiveData.value = user?.let {
                SplashViewState(isAuthenticated = true)
            } ?: SplashViewState(error = NoAuthException())
        }
    }
}