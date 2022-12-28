package com.example.appdevproject

import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.media.SoundPool
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
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
    private var mediaPlayers: HashMap<Int, MediaPlayer> = HashMap()

    fun load(audioID: Int, src: Int) {
        unload(audioID)
        val mediaPlayer = MediaPlayer.create(mContext, src)
        mediaPlayers[audioID] = mediaPlayer
    }

    fun load(audioID: Int, src: String) {
        // Still doesn't work :/
        return
//        unload(audioID)
//        try {
//            val mediaPlayer = MediaPlayer()
//            Log.d("NICE RON", "SRC: " + src)
//            val uri = Uri.parse(src)
//            mediaPlayer.setDataSource(mContext, uri)
//            mediaPlayer.prepare()
//            mediaPlayers[audioID] = mediaPlayer
//        }
//        catch (e: Exception) {
//            Log.e("NICE RON", "error while loading audio", e)
//        }
    }

    private fun unload(audioID: Int) {
        val mediaPlayer = mediaPlayers[audioID]
        if (mediaPlayer != null) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    fun play(audioID: Int, volume: Float) {
        mediaPlayers[audioID]?.let {
            it.setVolume(volume, volume)

            // Set playback speed
            val playbackParams = PlaybackParams()
            playbackParams.speed = getPlaybackRate()
            it.playbackParams = playbackParams

            it.start()
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

