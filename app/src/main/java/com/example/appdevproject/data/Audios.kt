package com.example.appdevproject.data

import android.content.res.Resources
import com.example.appdevproject.R

// ID's of default sound is negative, while DB is positive. This is for simplification
fun audios(): List<AudioData> {
    return listOf(
        AudioData(
            id = -1,
            title = "AA",
            src = R.raw.aa,
            custom = false,
        ),
        AudioData(
            id = -2,
            title = "EE",
            src = R.raw.ee,
            custom = false,
        ),
        AudioData(
            id = -3,
            title = "OO",
            src = R.raw.oo,
            custom = false,
        ),
        AudioData(
            id = -4,
            title = "Audio Jungle",
            src = R.raw.aj,
            custom = false,
        ),
        AudioData(
            id = -5,
            title = "AYAYAYA",
            src = R.raw.ayayaya,
            custom = false,
        ),
        AudioData(
            id = -6,
            title = "Sphee",
            src = R.raw.sphee,
            custom = false,
        ),
        AudioData(
            id = -7,
            title = "Nya",
            src = R.raw.nya,
            custom = false,
        ),
        AudioData(
            id = -8,
            title = "What happened?",
            src = R.raw.whathappened,
            custom = false,
        ),
        AudioData(
            id = -9,
            title = "Sneeze",
            src = R.raw.sneeze,
            custom = false,
        ),
        AudioData(
            id = -10,
            title = "Nice, Ron",
            src = R.raw.nr,
            custom = false,
        ),
        AudioData(
            id = -11,
            title = "What, I'm not allowed to sneeze?",
            src = R.raw.ws,
            custom = false,
        ),
        AudioData(
            id = -12,
            title = "Gas",
            src = R.raw.gas,
            custom = false,
        ),
        AudioData(
            id = -13,
            title = "Oooh, whoopsie",
            src = R.raw.ooh_whoopsie,
            custom = false,
        ),
        AudioData(
            id = -14,
            title = "aHHHHhwaheha",
            src = R.raw.ahhhhhwaheha,
            custom = false,
        ),
        AudioData(
            id = -15,
            title = "Zero",
            src = R.raw.zero,
            custom = false,
        ),
        AudioData(
            id = -16,
            title = "One",
            src = R.raw.one,
            custom = false,
        ),
        AudioData(
            id = -17,
            title = "EEEUUU",
            src = R.raw.eeeuuu,
            custom = false,
        ),
        AudioData(
            id = -18,
            title = "mmmNN",
            src = R.raw.mmmnn,
            custom = false,
        ),
        AudioData(
            id = -19,
            title = "urAE",
            src = R.raw.urae,
            custom = false,
        ),
        AudioData(
            id = -20,
            title = "ULTRA!",
            src = R.raw.ultra,
            custom = false,
        )
        ,AudioData(
            id = -21,
            title = "Ooey gooey cheesy!",
            src = R.raw.ooey,
            custom = false,
        ),
        /*AudioData(
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
        ),*/
    )
}