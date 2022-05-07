package com.example.appdevproject.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import com.example.appdevproject.db.DBHelper

class DataSource() {
    fun getAudioList(): List<AudioData> {
        /*val audioDB = arrayOf<AudioData>()
        val db = DBHelper(context, null)
        val cursor = db.getAudio()

        cursor!!.moveToFirst()
        do {
            val audio = AudioData(
                cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_SRC)),
            )
        } while (cursor.moveToNext())*/

        return audios()
    }
}