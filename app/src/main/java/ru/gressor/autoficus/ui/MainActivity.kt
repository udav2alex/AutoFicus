package ru.gressor.autoficus.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.gressor.autoficus.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        notes_recycler.layoutManager = LinearLayoutManager(this)
        adapter = NotesRVAdapter()
        notes_recycler.adapter = adapter

        viewModel.viewState().observe(this, Observer { state ->
            state?.let {
                adapter.notes = it.notes
            }
        })
    }
}