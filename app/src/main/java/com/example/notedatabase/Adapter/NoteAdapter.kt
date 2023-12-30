package com.example.notedatabase.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notedatabase.Model.Note
import com.example.notedatabase.R

//notes is coming from LiveData, in there it calls setNote() to set a new notes
//so every onCreate, notes get from LiveData
class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteAdapterHolder>() {

    private var notes: List<Note> = ArrayList()
    private var isItemMoved: Boolean = false

    class NoteAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //for later used in onBindViewHolder
        val tvTitle: TextView = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById<TextView>(R.id.tvDescription)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapterHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: NoteAdapterHolder, position: Int) {
        val currentNote: Note = notes[position]
        holder.tvTitle.text = currentNote.title
        holder.tvDescription.text = currentNote.description
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun setNote(notes: List<Note>) {
        this.notes = notes
        println(isItemMoved)
        if (!isItemMoved) {
            notifyDataSetChanged()
        }
        isItemMoved = false
    }

    fun getNote(position: Int): Note {
        return notes[position]
    }

    fun getNoteSize(): Int {
        return notes.size
    }

    fun isItemMoved(){
        println("set it to true")
        isItemMoved = true
    }
}