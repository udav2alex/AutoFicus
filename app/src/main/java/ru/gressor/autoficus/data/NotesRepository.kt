package ru.gressor.autoficus.data

import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.provider.FireDbDataProvider

object NotesRepository {

    private val dataProvider = FireDbDataProvider()

    fun getNotes() = dataProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
}