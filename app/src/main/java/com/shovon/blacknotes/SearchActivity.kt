package com.shovon.blacknotes

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.shovon.blacknotes.adapter.SearchAdapter
import com.shovon.blacknotes.model.SearchNotes
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.toolbar

class SearchActivity : AppCompatActivity() {

    var searchList = ArrayList<SearchNotes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        this.setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        searchView.isIconifiedByDefault = false
        searchView.requestFocus()
        rv_search.layoutManager = LinearLayoutManager(this)

        btn_back.setOnClickListener { super.onBackPressed() }

        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(sm.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQuery("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                loadQuery("%$newText%")
                return false
            }

        })
    }


    private fun loadQuery(title: String) {
        val dbManager = DbManager(this)
        val projections = arrayOf("id", "title", "description", "date", "time_stamp")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projections, "title like ?", selectionArgs, "title")
        searchList.clear()
        if (cursor.moveToFirst()) {

            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val noteTitle = cursor.getString(cursor.getColumnIndex("title"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                val date = cursor.getString(cursor.getColumnIndex("date"))

                searchList.add(
                    SearchNotes(
                        id,
                        noteTitle,
                        description,
                        date
                    )
                )

            } while (cursor.moveToNext())
        }
        if (searchList.size >= 1){
            rv_search.adapter =
                SearchAdapter(this, searchList)

        }
    }

    override fun onResume() {
        searchList.clear()
        super.onResume()
    }
}
