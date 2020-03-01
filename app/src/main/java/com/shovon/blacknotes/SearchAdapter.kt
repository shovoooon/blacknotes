package com.shovon.blacknotes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.notes_item.view.*
import java.text.SimpleDateFormat

class SearchAdapter(val context: Context, private val listofStatus: ArrayList<SearchNotes>) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listofStatus.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val status = listofStatus[position]
        holder.setData(status, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var currentStatus: SearchNotes? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                context.startActivity(Intent(context, AddNoteActivity::class.java)
                    .putExtra("id", currentStatus!!.noteId)
                    .putExtra("title", itemView.tv_note_title.text.toString())
                    .putExtra("description", itemView.tv_note_description.text.toString())
                )
            }
        }


        fun setData(status: SearchNotes?, position: Int) {
            status?.let {
                itemView.tv_note_title.text = status.noteTitle
                itemView.tv_note_description.text = status.noteDescription
                itemView.tv_note_date.text = status.date
                itemView.tv_id.text = status.noteId.toString()
            }

            this.currentStatus = status
            this.currentPosition = position
        }
    }
}