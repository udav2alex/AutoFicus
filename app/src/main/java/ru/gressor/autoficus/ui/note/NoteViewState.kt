package ru.gressor.autoficus.ui.note

import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.ui.base.BaseViewState

class NoteViewState(val note: Note? = null, error: Throwable? = null)
    : BaseViewState<Note?>(note, error) {


}