package com.example.appdevproject.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "soundboard.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "audios"

        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_SRC= "src"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY,
                $COL_TITLE TEXT,
                $COL_SRC TEXT
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME");
    }

    fun getAudio(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun addAudio(title: String, source: String) {
        val values = ContentValues()
        values.put(COL_TITLE, title)
        values.put(COL_SRC, source)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun editAudio(id: Int, title: String, source: String) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COL_TITLE, title)
        values.put(COL_SRC, source)

        db.update(
            TABLE_NAME, values,
            "$COL_ID IS ?", arrayOf<String>(id.toString())
        )
        db.close()
    }

    fun deleteAudio(id: Int) {
        val db = this.writableDatabase

        db.delete(
            TABLE_NAME,
            "$COL_ID IS ?",
            arrayOf<String>(id.toString())
        )
        db.close()
    }
}
