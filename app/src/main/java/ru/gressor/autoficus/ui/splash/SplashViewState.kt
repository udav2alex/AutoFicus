package ru.gressor.autoficus.ui.splash

import ru.gressor.autoficus.ui.base.BaseViewState

class SplashViewState(isAuthenticated: Boolean? = null, error: Throwable? = null) :
    BaseViewState<Boolean?>(isAuthenticated, error)