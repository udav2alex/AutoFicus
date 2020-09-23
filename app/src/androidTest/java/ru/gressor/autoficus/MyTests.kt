package ru.gressor.autoficus

import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import ru.gressor.autoficus.ui.note.NoteActivity

class MyTests {
    @get:Rule
    val activityTestRule =
        ActivityTestRule(NoteActivity::class.java, true, false)


}