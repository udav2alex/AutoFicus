package ru.gressor.autoficus.ui.main

import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.ui.base.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null)
    : BaseViewState<List<Note>?>(notes, error)