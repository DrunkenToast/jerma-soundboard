package com.example.appdevproject.api

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.appdevproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class APIHandler(private val context : Context) {
    var streamStatus : MutableLiveData<String> = MutableLiveData()
    private val scope = CoroutineScope(Dispatchers.Default)
    init {
        scope.launch {
            checkStreamStatus()
        }
    }
    private fun checkStreamStatus() {
        // Request returns html, not json. In a script tag there is JSON with the stream state.
        val url = URL(context.getString(R.string.twitch_url))

        streamStatus.postValue("Loading...")

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"

            BufferedReader(InputStreamReader(inputStream)).use {
                val status = it.readText().contains("\"isLiveBroadcast\":true")

                if (status) {
                    streamStatus.postValue(context.getString(R.string.live))
                }
                else {
                    streamStatus.postValue(context.getString(R.string.not_live))
                }
            }
        }
    }
}