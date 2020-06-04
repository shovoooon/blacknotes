package com.shovon.blacknotes

import android.app.Activity
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_note.*
import java.text.SimpleDateFormat
import java.util.*


class AddNoteActivity : AppCompatActivity() {

    val DB_NAME = "notes.db"
    val TABLE_NAME = "notes_table"
    val ID = "id"
    val TITLE = "title"
    val DESCRIPTION = "description"
    val DATE = "date"
    val TIMESTAMP = "time_stamp"
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        this.setSupportActionBar(toolbar)
        supportActionBar?.title = ""



        btn_back.setOnClickListener {
            if (et_title.text.isEmpty() && et_description.text.isEmpty()) {
                deleteNote()
            } else addFunc()
            super.onBackPressed()
        }

        btn_okay.setOnClickListener {
            if (et_title.text.isEmpty() && et_description.text.isEmpty()) {
                deleteNote()
            } else {
                addFunc()
                hideSoftKeyboard(this)
            }
        }

        try {
            val bundle: Bundle = intent.extras!!
            id = bundle.getInt("id", 0)
            if (id != 0) {
                et_title.setText(bundle.getString("title"))
                et_description.setText(bundle.getString("description"))
            }
        } catch (ex: Exception) {
        }

        btn_copy.setOnClickListener { copyNote() }
        btn_share.setOnClickListener { shareNote() }

    }

    private fun shareNote() {
        val title = et_title.text.toString()
        val desc = et_description.text.toString()
        val note = title + "\n" + desc
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, note)
        startActivity(Intent.createChooser(shareIntent, note))
    }

    private fun copyNote() {
        val title = et_title.text.toString()
        val desc = et_description.text.toString()
        val note = title + "\n" + desc
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.text = note
        Toast.makeText(this, "Note Copied", Toast.LENGTH_SHORT).show()
    }

    private fun deleteNote() {
        val dbManager = DbManager(this)
        val selectionArgs = arrayOf(id.toString())
        dbManager.delete("id=?", selectionArgs)
    }

    private fun addFunc() {
        val dbManager = DbManager(this)
        val i = SimpleDateFormat("YYYY MMM dd, HH:MM a").format(Date())
        val time_stamp = (System.currentTimeMillis() / 1000).toInt()
        val values = ContentValues()
        values.put(TITLE, et_title.text.toString())
        values.put(DESCRIPTION, et_description.text.toString())
        values.put(DATE, i)
        values.put(TIMESTAMP, time_stamp)

        if (id == 0) {
            val ID = dbManager.insert(values)
            if (ID > 0) {
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error saving note...", Toast.LENGTH_SHORT).show()
            }
        } else {
            val selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "id=?", selectionArgs)
            if (ID > 0) {
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error saving note...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideSoftKeyboard(activity: Activity) {

        try {
            val inputMethodManager: InputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken, 0
            )
        } catch (e: Exception) {
        }

        et_title.clearFocus()
        et_description.clearFocus()
    }
}
