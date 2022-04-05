package com.example.appdevproject

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast


class AudioService : Service() {

    private var audioServiceBinder: IBinder = AudioServiceBinder()

    override fun onBind(intent: Intent): IBinder {
        return audioServiceBinder
    }

    override fun onStart(intent: Intent?, startId: Int) {
        Log.d("debug", "Audio service started")
                Toast.makeText(this, "Audio service started",
                    Toast.LENGTH_LONG).show()
    }

}

class AudioServiceBinder : Binder() {
    val service: AudioServiceBinder
        get() = this@AudioServiceBinder

    val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(20)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        ).build()

}