package ru.gressor.autoficus.data

import ru.gressor.autoficus.data.entity.Note

object Repository {
    val notes: List<Note> = listOf(
        Note(
            title = "Первая заметка",
            text = "Текст Первая заметка"
        ),
        Note(
            title = "Вторая заметка",
            text = "Текст Вторая заметка",
            color = 0xff9575cd.toInt(),
            checked = true
        ),
        Note(
            title = "Третья заметка",
            text = "Текст Третья заметка",
            color = 0xff64b5f6.toInt()
        ),
        Note(
            title = "Четвертая заметка",
            text = "Текст Четвертая заметка",
            color = 0xff4db6ac.toInt(),
            checked = true
        ),
        Note(
            title = "Пятая заметка",
            text = "Текст Пятая заметка",
            color = 0xff9575cd.toInt()
        ),
        Note(
            title = "Шестая заметка",
            text = "Текст Шестая заметка",
            color = 0xff64b5f6.toInt()
        )
    )
}