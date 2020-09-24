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
import ru.gressor.autoficus.ui.common.DEBUG_TAG

class FireDbDataProvider(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth) : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    private val currentUser
        get() = auth.currentUser

    private val userNotesCollection =
        currentUser?.let {
            db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()

    override fun subscribeToAllNotes(): LiveData<RequestResult> =
        MutableLiveData<RequestResult>().apply {
            try {
                userNotesCollection
                    .addSnapshotListener { querySnapshot, error ->
                        this.value = error?.let {
                            Log.d(DEBUG_TAG, "Error in subscribeToAllNotes: ${it.message}")
                            return@let RequestResult.Error(it)
                        } ?: querySnapshot?.let {
                            Log.d(DEBUG_TAG, "subscribeToAllNotes")
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
                userNotesCollection.document(id).get()
                    .addOnSuccessListener { snapshot ->
                        Log.d(DEBUG_TAG, "getNoteById")
                        val note = snapshot.toObject(Note::class.java) as Note
                        this.value = RequestResult.Success(note)
                    }
                    .addOnFailureListener { error ->
                        Log.d(DEBUG_TAG, "Error in getNoteById: ${error.message}")
                        this.value = RequestResult.Error(error)
                    }
            } catch (error: Throwable) {
                this.value = RequestResult.Error(error)
            }
        }

    override fun deleteNote(id: String): LiveData<RequestResult> =
        MutableLiveData<RequestResult>().apply {
            try {
                Log.d(DEBUG_TAG, "deleteNote begin")
                userNotesCollection.document(id).delete()
                    .addOnSuccessListener {
                        Log.d(DEBUG_TAG, "deleteNote")
                        this.value = RequestResult.Success(null)
                    }
                    .addOnFailureListener { error ->
                        Log.d(DEBUG_TAG, "Error in deleteNote: ${error.message}")
                        this.value = RequestResult.Error(error)
                    }
            } catch (error: Throwable) {
                this.value = RequestResult.Error(error)
            }
        }

    override fun saveNote(note: Note): LiveData<RequestResult> =
        MutableLiveData<RequestResult>().apply {
            try {
                userNotesCollection.document(note.id).set(note)
                    .addOnSuccessListener {
                        this.value = RequestResult.Success(note)
                    }
                    .addOnFailureListener { error ->
                        Log.d(DEBUG_TAG, "Error in saveNote: ${error.message}")
                        this.value = RequestResult.Error(error)
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