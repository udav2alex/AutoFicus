package ru.gressor.autoficus.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.gressor.autoficus.R
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.ui.common.getColorResource

class MainAdapter(private val onItemClick: ((Note) -> Unit)?):
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    @FunctionalInterface
    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = with(itemView) {
            note_title.text = note.title
            note_text.text = note.text
            note_title.isChecked = note.checked
            setBackgroundColor(ContextCompat.getColor(itemView.context, getColorResource(note.color)))
            itemView.setOnClickListener { onItemClick?.invoke(note) }
        }
    }
}