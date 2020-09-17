package ru.gressor.autoficus.data

import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.provider.FireDbDataProvider
import ru.gressor.autoficus.data.provider.RemoteDataProvider

class NotesRepository(private val dataProvider: RemoteDataProvider) {

    fun getNotes() = dataProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun deleteNote(id: String) = dataProvider.deleteNote(id)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
    fun getCurrentUser() = dataProvider.getCurrentUser()
}