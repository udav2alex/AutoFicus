package ru.gressor.autoficus.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.errors.NoAuthException
import ru.gressor.autoficus.data.model.RequestResult
import ru.gressor.autoficus.data.model.User

class FireDbDataProvider : RemoteDataProvider {
    private val TAG = "${FireDbDataProvider::class.java.simpleName} :"

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    private val db = FirebaseFirestore.getInstance()
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    private fun getUserNotesCollection() =
        currentUser?.let {
            db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()

    override fun subscribeToAllNotes(): LiveData<RequestResult> =
        MutableLiveData<RequestResult>().apply {
            try {
                getUserNotesCollection()
                    .addSnapshotListener { querySnapshot, error ->
                        this.value = error?.let {
                            Log.d(TAG, "Error in subscribeToAllNotes: ${it.message}")
                            throw it
                        } ?: querySnapshot?.let {
                            val notes = it.documents.map { documentSnapshot ->
                                documentSnapshot.toObject(Note::class.java)
                            }
                            return@let RequestResult.Success(notes)
                        }
                    }
            } catch (error: Throwable) {
                this.value = RequestResult.Error(error)
            }
        }

    override fun getNoteById(id: String): LiveData<RequestResult> =
        MutableLiveData<RequestResult>().apply {
            try {
                getUserNotesCollection().document(id).get()
                    .addOnSuccessListener { snapshot ->
                        val note = snapshot.toObject(Note::class.java) as Note
                        this.value = RequestResult.Success(note)
                    }
                    .addOnFailureListener { error ->
                        Log.d(TAG, "Error in getNoteById: ${error.message}")
                        throw error
                    }
            } catch (error: Throwable) {
                this.value = RequestResult.Error(error)
            }
        }

    override fun saveNote(note: Note): LiveData<RequestResult> =
        MutableLiveData<RequestResult>().apply {
            try {
                getUserNotesCollection().document(note.id).set(note)
                    .addOnSuccessListener {
                        this.value = RequestResult.Success(note)
                    }
                    .addOnFailureListener { error ->
                        Log.d(TAG, "Error in saveNote: ${error.message}")
                        throw error
                    }
            } catch (error: Throwable) {
                this.value = RequestResult.Error(error)
            }
        }

    override fun getCurrentUser(): LiveData<User?> =
        MutableLiveData<User>().apply {
            this.value = currentUser?.let {
                User(it.displayName ?: "", it.email ?: "")
            }
        }
}