package com.shovon.blacknotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder

class DbManager(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "notes.db"
        const val TABLE_NAME = "notes_table"
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val DATE = "date"
        const val TIMESTAMP = "time_stamp"
        const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$TITLE TEXT," +
                    "$DESCRIPTION TEXT," +
                    "$DATE TEXT," +
                    "$TIMESTAMP INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun insert(values: ContentValues): Long {
        val sqlDB = this.writableDatabase
        return sqlDB!!.insert(TABLE_NAME, null, values)
    }

    fun query(
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sorOrder: String
    ): Cursor {
        val sqlDB = this.writableDatabase
        val qb = SQLiteQueryBuilder()
        qb.tables = TABLE_NAME
        return qb.query(sqlDB, projection, selection, selectionArgs, null, null, sorOrder)
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        val sqlDB = this.writableDatabase
        return sqlDB!!.update(TABLE_NAME, values, selection, selectionArgs)
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        val sqlDB = this.writableDatabase
        return sqlDB!!.delete(TABLE_NAME, selection, selectionArgs)
    }

    val showData: Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $TIMESTAMP DESC", null)
        }
}