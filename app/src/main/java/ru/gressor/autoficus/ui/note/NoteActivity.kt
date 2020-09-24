package ru.gressor.autoficus.ui.note

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.gressor.autoficus.R
import ru.gressor.autoficus.data.entity.Color
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.ui.base.BaseActivity
import ru.gressor.autoficus.ui.common.format
import ru.gressor.autoficus.ui.common.getColorInt
import java.util.*

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {

    override val layoutRes = R.layout.activity_note
    override val viewModel: NoteViewModel by viewModel()

    private var note: Note? = null
    private var color: Color = Color.BLUE

    companion object {
        val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun start(context: Context, noteId: String? = null) =
            Intent(context, NoteActivity::class.java).run {
                noteId?.let { putExtra(EXTRA_NOTE, noteId) }
                context.startActivity(this)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let { viewModel.loadNote(it) }

        initView()
    }

    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) { saveNote() }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun saveNote() {
        if (note_title.text == null || note_title.text!!.length < 3) return

        note = note?.copy(
            title = note_title.text.toString(),
            text = note_text.text.toString(),
            lastChanged = Date(),
            color = color
        ) ?: Note(
            UUID.randomUUID().toString(),
            note_title.text.toString(),
            note_text.text.toString()
        )

        note?.let {
            viewModel.saveChanges(it)
        }
    }

    private fun initView() {
        note_title.removeTextChangedListener(textChangeListener)
        note_text.removeTextChangedListener(textChangeListener)

        note?.run {
            supportActionBar?.title = lastChanged.format()
            note_title.setText(title)
            note_text.setText(text)
            note_title.setSelection(note_title.text?.length ?: 0)
            note_text.setSelection(note_text.text?.length ?: 0)
            toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))
        } ?: run {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        note_title.addTextChangedListener(textChangeListener)
        note_text.addTextChangedListener(textChangeListener)

        colorPicker.onColorClickListener = {
            color = it
            toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))
            saveNote()
        }
    }

    override fun renderData(data: NoteViewState.Data) {
        if(data.isDeleted) finish()
        this.note = data.note
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu?) =
        menuInflater.inflate(R.menu.note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        R.id.palette -> togglePalette().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.note_delete_message))
            .setPositiveButton(android.R.string.ok) { _, _ -> viewModel.deleteNote() }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onBackPressed() {
        if (colorPicker.isOpen) {
            colorPicker.close()
            return
        }
        super.onBackPressed()
    }
}