package ru.gressor.autoficus.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.gressor.autoficus.R
import ru.gressor.autoficus.data.entity.Note
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
        // TODO: почему сюда не подходит лямбда (note) -> NoteActivity.start(this@MainActivity, note)
        adapter = MainAdapter(object : MainAdapter.OnItemClickListener {
            override fun onItemClick(note: Note)
                    = NoteActivity.start(this@MainActivity, note)
        })
        notes_recycler.adapter = adapter

        fab.setOnClickListener {
            NoteActivity.start(this@MainActivity, null)
        }
    }
}