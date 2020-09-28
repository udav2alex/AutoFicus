package ru.gressor.autoficus.data.provider

import kotlinx.coroutines.channels.ReceiveChannel
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.model.RequestResult
import ru.gressor.autoficus.data.model.User

interface RemoteDataProvider {
    fun subscribeToAllNotes(): ReceiveChannel<RequestResult>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note): Note
    suspend fun deleteNote(id: String)
    suspend fun getCurrentUser(): User?
}