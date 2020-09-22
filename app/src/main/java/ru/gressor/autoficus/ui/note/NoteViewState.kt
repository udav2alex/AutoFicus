package ru.gressor.autoficus.ui.note

import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null)
    : BaseViewState<NoteViewState.Data>(data, error) {

    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}