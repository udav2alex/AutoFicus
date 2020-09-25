package ru.gressor.autoficus.data.provider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.errors.NoAuthException
import ru.gressor.autoficus.data.model.RequestResult
import ru.gressor.autoficus.data.model.User
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    @ExperimentalCoroutinesApi // invokeOnClose
    override fun subscribeToAllNotes(): ReceiveChannel<RequestResult> =
        Channel<RequestResult>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null

            try {
                registration = userNotesCollection
                    .addSnapshotListener { querySnapshot, error ->

                        val result = error?.let {
                            RequestResult.Error(it)
                        } ?: querySnapshot.let {
                            val allNotes = it?.map {queryDocumentSnapshot ->
                                queryDocumentSnapshot.toObject(Note::class.java)
                            }
                            RequestResult.Success(allNotes)
                        }

                        offer(result)
                    }
            } catch (e: Throwable) {
                offer(RequestResult.Error(e))
            }

            invokeOnClose { registration?.remove() }
        }

    override suspend fun getNoteById(id: String): Note =
        suspendCoroutine {continuation ->
            try {
                userNotesCollection.document(id).get()
                    .addOnSuccessListener {
                        continuation.resume(it.toObject(Note::class.java)!!)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }

    override suspend fun deleteNote(id: String): Unit =
        suspendCoroutine {continuation ->
            try {
                userNotesCollection.document(id).delete()
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }

    override suspend fun saveNote(note: Note): Note =
        suspendCoroutine {continuation ->
            try {
                userNotesCollection.document(note.id).set(note)
                    .addOnSuccessListener {
                        continuation.resume(note)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }

    override suspend fun getCurrentUser(): User? =
        suspendCoroutine {continuation ->
            try {
                currentUser?.let {
                    continuation.resume(User(it.displayName ?: "", it.email ?: ""))
                }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }
}