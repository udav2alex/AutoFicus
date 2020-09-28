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
    private val auth: FirebaseAuth
) : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    private val currentUser
        get() = auth.currentUser

    private val userNotesCollection
        get() = currentUser?.let {
                db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
            } ?: throw NoAuthException()

    override fun subscribeToAllNotes(): LiveData<RequestResult> =
        MutableLiveData<RequestResult>().apply {
            try {
                userNotesCollection
                    .addSnapshotListener { querySnapshot, error ->
                        this.value = error?.let {
                            return@let RequestResult.Error(it)
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
                userNotesCollection.document(id).get()
                    .addOnSuccessListener { snapshot ->
                        val note = snapshot.toObject(Note::class.java) as Note
                        this.value = RequestResult.Success(note)
                    }
                    .addOnFailureListener { error ->
                        this.value = RequestResult.Error(error)
                    }
            } catch (error: Throwable) {
                this.value = RequestResult.Error(error)
            }
        }

    override fun deleteNote(id: String): LiveData<RequestResult> =
        MutableLiveData<RequestResult>().apply {
            try {
                userNotesCollection.document(id).delete()
                    .addOnSuccessListener {
                        this.value = RequestResult.Success(null)
                    }
                    .addOnFailureListener { error ->
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