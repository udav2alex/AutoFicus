package ru.gressor.autoficus.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.model.RequestResult

class FireDbDataProvider : RemoteDataProvider {
    private val TAG = "${FireDbDataProvider::class.java.simpleName} :"

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<RequestResult> {
        val result = MutableLiveData<RequestResult>()

        notesReference.addSnapshotListener { querySnapshot, error ->
            error?.let {
                result.value = RequestResult.Error(it)
            } ?: let {
                querySnapshot?.let {
                    val notes = it.documents.map {documentSnapshot ->
                        documentSnapshot.toObject(Note::class.java)
                    }
                    result.value = RequestResult.Success(notes)
                }
            }
        }

        return result
    }

    override fun getNoteById(id: String): LiveData<RequestResult> {
        val result = MutableLiveData<RequestResult>()

        notesReference.document(id).get()
            .addOnSuccessListener {snapshot ->
                val note = snapshot.toObject(Note::class.java) as Note
                Log.d(TAG, "Note $note is loaded!")
                result.value = RequestResult.Success(note)
            }
            .addOnFailureListener { error ->
                result.value = RequestResult.Error(error)
            }

        return result
    }

    override fun saveNote(note: Note): LiveData<RequestResult> {
        val result = MutableLiveData<RequestResult>()

        notesReference.document(note.id).set(note)
            .addOnSuccessListener {
                Log.d(TAG, "Note $note is saved!")
                result.value = RequestResult.Success(note)
            }
            .addOnFailureListener {error ->
                Log.d(TAG, "Error saving note $note: ${error.message}")
                result.value = RequestResult.Error(error)
            }

        return result
    }
}