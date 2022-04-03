package com.example.appdevproject.data

import android.content.res.Resources
import com.example.appdevproject.R

fun audios(resources: Resources): List<AudioData> {
    return listOf(
        AudioData(
            id = 1,
            title = "Birds",
            src = R.raw.birds
        ),
        AudioData(
            id = 2,
            title = "Boat",
            src = R.raw.boat
        ),
        AudioData(
            id = 3,
            title = "City",
            src = R.raw.city
        ),
        AudioData(
            id = 4,
            title = "Coffee shop",
            src = R.raw.coffeeshop
        ),
        AudioData(
            id = 5,
            title = "Fireplace",
            src = R.raw.fireplace
        ),
        AudioData(
            id = 6,
            title = "Pink noise",
            src = R.raw.pinknoise
        ),
    )
}