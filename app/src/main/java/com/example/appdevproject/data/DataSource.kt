package com.example.appdevproject.data

import android.content.res.Resources

class DataSource(private val resources: Resources) {
    fun getAudioList(): List<AudioData> {
        return audios(resources)
    }
}