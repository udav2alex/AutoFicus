package ru.gressor.autoficus.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_note.*
import ru.gressor.autoficus.R
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.ui.base.BaseActivity
import ru.gressor.autoficus.ui.common.DATE_TIME_FORMAT
import ru.gressor.autoficus.ui.common.format
import ru.gressor.autoficus.ui.common.getColorInt
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    override val layoutRes = R.layout.activity_note

    override val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    private var note: Note? = null

    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) { saveNote() }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun start(context: Context, noteId: String? = null) =
            Intent(context, NoteActivity::class.java).run {
                noteId?.let {
                    putExtra(EXTRA_NOTE, noteId)
                }
                context.startActivity(this)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let {
            viewModel.loadNote(it)
        }

        initView()
    }

    private fun saveNote() {
        if (note_title.text == null || note_title.text!!.length < 3) return

        note = note?.copy(
            title = note_title.text.toString(),
            text = note_text.text.toString(),
            lastChanged = Date()
        ) ?: Note(
            UUID.randomUUID().toString(),
            note_title.text.toString(),
            note_text.text.toString()
        )

        note?.let {
            viewModel.saveChanges(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initView() {
        note_title.removeTextChangedListener(textChangeListener)
        note_text.removeTextChangedListener(textChangeListener)

        note?.run {
            supportActionBar?.title = lastChanged.format()
            note_title.setText(title)
            note_text.setText(text)
            toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))
        } ?: run {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        note_title.addTextChangedListener(textChangeListener)
        note_text.addTextChangedListener(textChangeListener)
    }

    override fun renderData(data: Note?) {
        this.note = data
        initView()
    }
}