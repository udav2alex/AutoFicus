package ru.gressor.autoficus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.gressor.autoficus.data.entity.Color
import ru.gressor.autoficus.data.entity.Note
import java.util.*

object NotesRepository {

    private val notesLiveData = MutableLiveData<List<Note>>()

    private val notes: MutableList<Note> = mutableListOf(
        Note(id = UUID.randomUUID().toString(),
            title = "Первая заметка",
            text = "Текст Первая заметка"
        ),
        Note(id = UUID.randomUUID().toString(),
            title = "Вторая заметка",
            text = "Текст Вторая заметка",
            color = Color.BLUE,
            checked = true
        ),
        Note(id = UUID.randomUUID().toString(),
            title = "Третья заметка",
            text = "Текст Третья заметка",
            color = Color.GREEN
        ),
        Note(id = UUID.randomUUID().toString(),
            title = "Четвертая заметка",
            text = "Текст Четвертая заметка",
            color = Color.PINK,
            checked = true
        ),
        Note(id = UUID.randomUUID().toString(),
            title = "Пятая заметка",
            text = "Текст Пятая заметка",
            color = Color.VIOLET
        ),
        Note(id = UUID.randomUUID().toString(),
            title = "Шестая заметка",
            text = "Текст Шестая заметка"
        )
    )

    init {
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> = notesLiveData

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note) {
        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)

    }
}