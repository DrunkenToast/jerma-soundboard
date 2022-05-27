package com.example.appdevproject

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.AudioAttributes
import android.media.SoundPool
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.loader.content.CursorLoader
import androidx.preference.PreferenceManager

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
        // TODO: Crashes because of permissions (or something else)
        //loadedIDs[audioID] = soundPool.load(getRealPathFromURI(mContext, src), 1)
    }

    private fun unload(audioID: Int) {
        val soundID = loadedIDs[audioID]
        if (soundID != null)
            soundPool.unload(soundID)
    }

    fun play(audioID: Int, volume: Float) {
        loadedIDs[audioID]?.let {
            soundPool.play(it, volume, volume, 1, 0, getPlaybackRate())
        }
    }

    private fun getPlaybackRate(): Float {
        return PreferenceManager.getDefaultSharedPreferences(this)
            .getString(getString(R.string.playback_pref), "1").toString().toFloatOrNull()
            ?: return 1F
    }

    inner class AudioServiceBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }

    // TODO doesn't work, have tried debugging this for a long time now :)
    // Permissions are hard :/
    // https://stackoverflow.com/questions/35529124/android-soundpool-error-while-loading-file
    private fun getRealPathFromURI(context: Context, uriString: String): String {
        val proj = arrayOf(MediaStore.Audio.Media.DATA, MediaStore.Downloads.DATA)
        val loader = CursorLoader(context, Uri.parse(uriString), null, null, null, null)
        val cursor: Cursor? = loader.loadInBackground()
        cursor!!.moveToFirst()
        val column_index: Int = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
}

