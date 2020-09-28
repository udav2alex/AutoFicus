package ru.gressor.autoficus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnSuccessListener

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*

import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.data.errors.NoAuthException
import ru.gressor.autoficus.data.model.RequestResult
import ru.gressor.autoficus.data.provider.FireDbDataProvider

class FireDbDataProviderTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockDb = mockk<FirebaseFirestore>() // relaxed = true ?
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockResultCollection = mockk<CollectionReference>()
    private val mockUser = mockk<FirebaseUser>()

    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()

    private val testNotes = listOf(Note("1"), Note("2"), Note("3"))

    private val provider = FireDbDataProvider(mockDb, mockAuth)

    @Before
    fun setup() {
        clearAllMocks()
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns ""

        every { mockDb.collection(any()).document(any()).collection(any()) } returns mockResultCollection

        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]
    }

    @Test
    fun `should throw if no auth`() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null

        provider.subscribeToAllNotes().observeForever {
            result = (it as? RequestResult.Error)?.error
        }

        assertTrue(result is NoAuthException)
    }

    @Test
    fun `subscribeAllNotes return notes`() {
        var result: List<Note>? = null
        val slot = slot<EventListener<QuerySnapshot>>()
        val mockSnapshot = mockk<QuerySnapshot>()
        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever {
            @Suppress("UNCHECKED_CAST")
            result = (it as? RequestResult.Success<List<Note>>)?.data
        }
        slot.captured.onEvent(mockSnapshot, null)

        assertEquals(testNotes, result)
    }

    @Test
    fun `subscribeToAllNotes returns error`() {
        var result: Throwable? = null
        val mockException = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever {
            result = (it as? RequestResult.Error)?.error
        }
        slot.captured.onEvent(null, mockException)

        assertEquals(result, mockException)
    }

    @Test
    fun `saveNote calls set once`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

        provider.saveNote(testNotes[0])

        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote returns Note`() {
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<in Void>>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)) } returns mockk()

        provider.saveNote(testNotes[0]).observeForever {
            @Suppress("UNCHECKED_CAST")
            result = (it as? RequestResult.Success<Note>)?.data
        }

        slot.captured.onSuccess(null)
        assertEquals(result, testNotes[0])
    }

    @Test
    fun `deleteNote returns Success`() {
        var result: RequestResult? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<in Void>>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.delete().addOnSuccessListener(capture(slot)) } returns mockk()

        provider.deleteNote(testNotes[0].id).observeForever {
            result = (it as? RequestResult.Success<*>)
        }

        slot.captured.onSuccess(null)
        assertEquals(result, RequestResult.Success(null))
    }
}