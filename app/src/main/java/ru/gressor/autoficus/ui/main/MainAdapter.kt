package ru.gressor.autoficus.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*
import ru.gressor.autoficus.R
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.ui.common.getColorRes

class MainAdapter(private val onItemClick: ((Note) -> Unit)?):
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_note,
                parent,
                false
            )
        )

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(note: Note) {
            note_title.text = note.title
            note_text.text = note.text
            note_title.isChecked = note.checked
            containerView.setBackgroundColor(note.color.getColorRes(containerView.context))
            containerView.setOnClickListener { onItemClick?.invoke(note) }
        }
    }
}