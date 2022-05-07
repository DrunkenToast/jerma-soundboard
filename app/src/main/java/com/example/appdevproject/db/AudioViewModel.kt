package com.example.appdevproject.db

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appdevproject.data.AudioDataDB
import java.lang.Exception

class AudioViewModel: ViewModel() {
    val AudioDBList: MutableLiveData<MutableList<AudioDataDB>> = MutableLiveData<MutableList<AudioDataDB>>()

    fun loadAudio(c: Context) {
        Log.d("NICE RON", "Loading audio")
        val list = mutableListOf<AudioDataDB>()
        val db = DBHelper(c, null)

        val cursor = db.getAudio()
        cursor.moveToFirst()
        try {
            do {
                val audio = AudioDataDB(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_SRC)),
                )
                list.add(audio)
            } while (cursor.moveToNext())
        }
        catch (e: Exception) {
            list.clear()
        }

        AudioDBList.value = list
    }
}