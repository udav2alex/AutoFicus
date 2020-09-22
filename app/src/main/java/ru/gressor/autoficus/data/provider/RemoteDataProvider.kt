package ru.gressor.autoficus.data.provider

import androidx.lifecycle.LiveData
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.model.RequestResult
import ru.gressor.autoficus.data.model.User

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<RequestResult>
    fun getNoteById(id: String): LiveData<RequestResult>
    fun saveNote(note: Note): LiveData<RequestResult>
    fun deleteNote(id: String): LiveData<RequestResult>
    fun getCurrentUser(): LiveData<User?>
}