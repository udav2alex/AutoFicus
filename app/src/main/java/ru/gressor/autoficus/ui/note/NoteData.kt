package ru.gressor.autoficus.ui.note

import ru.gressor.autoficus.data.entity.Note

data class NoteData(val isDeleted: Boolean = false, val note: Note? = null)