package com.example.appdevproject

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast


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

    fun load(audioID: Int, audio: Int) {
        loadedIDs[audioID] = soundPool.load(mContext, audio, 1)
    }

    fun play(audioID: Int, volume: Float) {
        loadedIDs[audioID]?.let {
            Log.d("TAG", "S")
            soundPool.play(it, volume, volume, 1, 0, 1.0F)
        }
        Toast.makeText(mContext, "playing",
            Toast.LENGTH_SHORT).show()
    }


    inner class AudioServiceBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }
}

