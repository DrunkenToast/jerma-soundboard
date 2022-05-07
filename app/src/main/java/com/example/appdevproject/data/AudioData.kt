package com.example.appdevproject.data

data class AudioData(
    val id: Int,
    val title: String,
    val src: Int,
    val custom: Boolean,
)

data class AudioDataDB(
    val id: Int,
    val title: String,
    val src: String,
)