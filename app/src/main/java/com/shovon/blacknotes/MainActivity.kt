package com.shovon.blacknotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.shovon.blacknotes.adapter.NotesAdapter
import com.shovon.blacknotes.model.Notes
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Notes>()
    private var BACK = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        rv_notes.layoutManager = LinearLayoutManager(this)

        showData()

        fab.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
        btn_search.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
    }

    private fun showData() {
        listNotes.clear()
        val dbManager = DbManager(this)
        val res = dbManager.showData
        if (res.count == 0) {
            Toast.makeText(this, "Hey welcome!", Toast.LENGTH_SHORT).show()
        } else {
            while (res.moveToNext()) {

                val id = res.getInt(0)
                val title = res.getString(1)
                val description = res.getString(2)
                val date = res.getString(3)

                listNotes.add(
                    Notes(
                        id,
                        title,
                        description,
                        date
                    )
                )
            }

        }

        if (listNotes.size >= 1) {
            rv_notes.adapter =
                NotesAdapter(this, listNotes)

        }
    }

    override fun onResume() {
        showData()
        super.onResume()
    }

    override fun onBackPressed() {
        if (BACK) {
            super.onBackPressed()
        } else {
            BACK = true
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
    }
}
