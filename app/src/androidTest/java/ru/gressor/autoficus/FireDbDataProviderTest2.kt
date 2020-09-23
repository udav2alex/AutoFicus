package ru.gressor.autoficus

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert.*
import ru.gressor.autoficus.data.errors.NoAuthException
import ru.gressor.autoficus.data.model.RequestResult
import ru.gressor.autoficus.data.provider.FireDbDataProvider

class FireDbDataProviderTest2 {

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockUser = mockk<FirebaseUser>()

    private val provider = FireDbDataProvider(mockDb, mockAuth)

    @Before
    fun setup() {
        clearAllMocks()
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns ""
    }

    @Test
    fun should_throw_if_no_auth() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null

        provider.subscribeToAllNotes().observeForever {
            result = (it as? RequestResult.Error)?.error
        }

        assertTrue(result is NoAuthException)
    }
}