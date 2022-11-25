package com.example.appdevproject.db

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "soundboard.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "audios"

        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_SRC= "src"

        const val AUTHORITY = "com.example.appdevproject"
        val BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY)
        const val AUDIO_PATH = "audio"

        const val ALL_AUDIO = 100
        const val AUDIO_ID = 101

        fun buildUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(AUTHORITY, AUDIO_PATH, ALL_AUDIO)
            uriMatcher.addURI(AUTHORITY, "/#", AUDIO_ID)

            return uriMatcher
        }
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

    fun addAudio(values: ContentValues): Uri {
//        val values = ContentValues()
//        values.put(COL_TITLE, title)
//        values.put(COL_SRC, source)

        val db = this.writableDatabase
        val rowId = db.insert(TABLE_NAME, null, values)
        if (rowId > 0) {
            val uri = ContentUris.withAppendedId(BASE_CONTENT_URI, rowId)
            db.close()
            return uri
        }
        throw SQLiteException("Failed to add record")
    }

    fun editAudio(values: ContentValues?, whereClause: String?, whereArgs: Array<out String>?): Int {
//        fun editAudio(id: Int, title: String, source: String) {
        val db = this.writableDatabase

//        val values = ContentValues()
//        values.put(COL_TITLE, title)
//        values.put(COL_SRC, source)

//        db.update(
//            TABLE_NAME, values,
//            "$COL_ID IS ?", arrayOf<String>(id.toString())
//        )

        val cnt = db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
        return cnt
    }

    fun deleteAudio(whereClause: String?, whereArgs: Array<out String>?): Int {
        val db = this.writableDatabase

//        db.delete(
//            TABLE_NAME,
//            "$COL_ID IS ?",
//            arrayOf<String>(id.toString())
//        )
//        db.close()

        val cnt = db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
        return cnt
    }
}
