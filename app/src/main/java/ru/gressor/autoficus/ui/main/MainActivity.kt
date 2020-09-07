package ru.gressor.autoficus.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.gressor.autoficus.R
import ru.gressor.autoficus.ui.note.NoteActivity

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.viewState().observe(this, { state ->
            state?.let {
                adapter.notes = it.notes
            }
        })

        notes_recycler.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter { NoteActivity.start(this@MainActivity, it) }
        notes_recycler.adapter = adapter

        fab.setOnClickListener {
            NoteActivity.start(this@MainActivity)
        }
    }
}