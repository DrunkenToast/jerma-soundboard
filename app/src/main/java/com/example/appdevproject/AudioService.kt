package com.example.appdevproject

import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.AudioAttributes
import android.media.SoundPool
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.loader.content.CursorLoader


class AudioService : Service() {
    private var audioServiceBinder: IBinder = AudioServiceBinder()
    private lateinit var mContext: Context
    override fun onBind(intent: Intent): IBinder {
        return audioServiceBinder
    }

    override fun onStart(intent: Intent?, startId: Int) {
        Log.d("debug", "Audio service started")
                Toast.makeText(this, "Audio service started",
                    Toast.LENGTH_LONG).show()
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    // AudioID : loadID
    private var loadedIDs: HashMap<Int, Int> = HashMap()

    private var soundPool: SoundPool = SoundPool.Builder()
    .setMaxStreams(20)
    .setAudioAttributes(
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
    ).build()

    fun load(audioID: Int, src: Int) {
        unload(audioID)
        loadedIDs[audioID] = soundPool.load(mContext, src, 1)
    }

    fun load(audioID: Int, src: String) {
        unload(audioID)
        Log.d("TAG", "$audioID load called")
        loadedIDs[audioID] = soundPool.load(getRealPathFromURI(this.mContext, src), 1)
    }

    private fun unload(audioID: Int) {
        val soundID = loadedIDs[audioID]
        if (soundID != null)
            soundPool.unload(soundID)
    }

    fun play(audioID: Int, volume: Float) {
        loadedIDs[audioID]?.let {
            Log.d("TAG", "Found")
            soundPool.play(it, volume, volume, 1, 0, 1.0F)
        }
        Toast.makeText(mContext, "playing",
            Toast.LENGTH_SHORT).show()
    }


    inner class AudioServiceBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }

    // https://stackoverflow.com/questions/35529124/android-soundpool-error-while-loading-file
    private fun getRealPathFromURI(context: Context, uriString: String): String {
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        val loader = CursorLoader(context, Uri.parse(uriString), proj, null, null, null)
        val cursor: Cursor? = loader.loadInBackground()
        cursor!!.moveToFirst()
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
}

