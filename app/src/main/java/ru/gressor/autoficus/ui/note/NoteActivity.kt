package ru.gressor.autoficus.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_note.*
import ru.gressor.autoficus.R
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.ui.common.DATE_TIME_FORMAT
import ru.gressor.autoficus.ui.common.getColorResource
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity: AppCompatActivity() {

    private var note: Note? = null
    private lateinit var viewModel: NoteViewModel

    val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun start(context: Context, note: Note?) =
            Intent(context, NoteActivity::class.java).run {
                note?.let {
                    putExtra(EXTRA_NOTE, note)
                }
                context.startActivity(this)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        note = intent.getParcelableExtra(EXTRA_NOTE)

        initActionBar()
        initView()
    }

    private fun saveNote() {
        if (note_title.text == null || note_title.text!!.length < 3) return

        note = note?.copy(
            title = note_title.text.toString(),
            text = note_text.text.toString(),
            lastChanged = Date()
        ) ?: Note(UUID.randomUUID().toString(), note_title.text.toString(), note_text.text.toString())

        note?.let {
            viewModel.save(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initView() {
        note_title.removeTextChangedListener(textChangeListener)
        note_text.removeTextChangedListener(textChangeListener)

        note?.let{
            note_title.setText(it.title)
            note_text.setText(it.text)
            toolbar.setBackgroundColor(this@NoteActivity.getColor(getColorResource(note!!.color)))
        }

        note_title.addTextChangedListener(textChangeListener)
        note_text.addTextChangedListener(textChangeListener)
    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (note == null) {
            getString(R.string.new_note_title)
        } else {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
                .format(note!!.lastChanged)
        }
    }
}