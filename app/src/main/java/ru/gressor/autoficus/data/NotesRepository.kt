package ru.gressor.autoficus.data

import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.provider.FireDbDataProvider
import ru.gressor.autoficus.data.provider.RemoteDataProvider

class NotesRepository(private val dataProvider: RemoteDataProvider) {

    fun getNotes() = dataProvider.subscribeToAllNotes()
    suspend fun saveNote(note: Note) = dataProvider.saveNote(note)
    suspend fun deleteNote(id: String) = dataProvider.deleteNote(id)
    suspend fun getNoteById(id: String) = dataProvider.getNoteById(id)
    suspend fun getCurrentUser() = dataProvider.getCurrentUser()
}