package com.example.appdevproject.db

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils

class AudioContentProvider : ContentProvider() {
    private lateinit var mDBHelper: DBHelper
    companion object {
        var AudioMap = HashMap<String, String>()

        const val AUTHORITY = "com.example.appdevproject"
        val BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY)
        const val AUDIO_PATH = "audio"

        const val ALL_AUDIO = 100
        const val AUDIO_ID = 101

        fun buildUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(AUTHORITY, AUDIO_PATH, ALL_AUDIO)
            uriMatcher.addURI(AUTHORITY, "$AUDIO_PATH/#", AUDIO_ID)

            return uriMatcher
        }
    }

    override fun onCreate(): Boolean {
        //TODO: fix !!?
        mDBHelper = DBHelper(context!!, null);

        return false;
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        _sortOrder: String?
    ): Cursor? {
        val qb = SQLiteQueryBuilder()
        var sortOrder = _sortOrder
        qb.tables = DBHelper.TABLE_NAME

        when (buildUriMatcher().match(uri)) {
            ALL_AUDIO ->
                qb.projectionMap = AudioMap
            AUDIO_ID ->
                qb.appendWhere(
                    DBHelper.COL_ID + "=" + uri.pathSegments[1]
                )
            else -> throw java.lang.IllegalArgumentException("Unsupported URI: $uri")
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = DBHelper.COL_TITLE
        }

        val c = qb.query(
            mDBHelper.readableDatabase, projection,
            selection, selectionArgs, null, null, sortOrder
        )
        c.setNotificationUri(context?.contentResolver, uri)
        return c
    }

    override fun getType(uri: Uri): String? {
        when(buildUriMatcher().match(uri)) {
            AUDIO_ID or ALL_AUDIO ->
                return "vnd.android.cursor.dir/$AUDIO_PATH"
            else -> throw java.lang.IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = mDBHelper.writableDatabase
        val rowId = db.insert(DBHelper.TABLE_NAME, "", values)
        if (rowId > 0) {
            val newUri = ContentUris.withAppendedId(BASE_CONTENT_URI, rowId)
            return newUri
        }
        db.close()
        throw SQLiteException("Failed to add record")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val cnt: Int
        when(buildUriMatcher().match(uri)) {
            ALL_AUDIO -> {
                val db = mDBHelper.writableDatabase
                cnt = db.delete(DBHelper.TABLE_NAME, selection, selectionArgs)
                db.close()
            }
            AUDIO_ID -> {
                val db = mDBHelper.writableDatabase
                val id = uri.lastPathSegment
                var s = ""
                if (!TextUtils.isEmpty(selection)) {
                    s = " AND ($selection)"
                }
                cnt = db.delete(
                    DBHelper.TABLE_NAME,
                    DBHelper.COL_ID+"="+id+s,
                    selectionArgs
                )
                db.close()
            }
            else -> throw java.lang.IllegalArgumentException("Unsupported URI: $uri")
        }
        return cnt
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val cnt: Int
        when(buildUriMatcher().match(uri)) {
            ALL_AUDIO -> {
                val db = mDBHelper.writableDatabase
                cnt = db.update(DBHelper.TABLE_NAME, values, selection, selectionArgs)
                db.close()
            }
            AUDIO_ID -> {
                val db = mDBHelper.writableDatabase
                val id = uri.lastPathSegment
                var s = ""
                if (!TextUtils.isEmpty(selection)) {
                    s = " AND ($selection)"
                }
                cnt = db.update(
                    DBHelper.TABLE_NAME,
                    values,
                    DBHelper.COL_ID+"="+id+s,
                    selectionArgs
                )
                db.close()
            }
            else -> throw java.lang.IllegalArgumentException("Unsupported URI: $uri")
        }
        return cnt
    }
}
