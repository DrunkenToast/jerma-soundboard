package com.example.appdevproject.db

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appdevproject.audioList.AudioAdapter.AudioItem
import com.example.appdevproject.data.AudioDataDB
import java.lang.Exception

class AudioViewModel: ViewModel() {
    val AudioDBList: MutableLiveData<MutableList<AudioDataDB>> = MutableLiveData<MutableList<AudioDataDB>>()

    fun loadAudio(c: Context) {
        Log.d("NICE RON", "Loading audio")
        val list = mutableListOf<AudioDataDB>()
        val db = DBHelper(c, null)

//        val cursor = db.getAudio()
        val cursor = c.contentResolver.query(
            Uri.withAppendedPath(
                AudioContentProvider.BASE_CONTENT_URI,
                AudioContentProvider.AUDIO_PATH
            ), null, null, null, null,null
        )

        if (cursor == null) {
            list.clear()
            return
        }

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