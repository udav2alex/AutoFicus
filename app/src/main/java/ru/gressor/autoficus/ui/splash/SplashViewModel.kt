package ru.gressor.autoficus.ui.splash

import ru.gressor.autoficus.data.NotesRepository
import ru.gressor.autoficus.data.errors.NoAuthException
import ru.gressor.autoficus.ui.base.BaseViewModel

class SplashViewModel: BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        NotesRepository.getCurrentUser().observeForever {user ->
            viewStateLiveData.value = user?.let {
                SplashViewState(isAuthenticated = true)
            } ?: SplashViewState(error = NoAuthException())
        }
    }
}