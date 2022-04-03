package com.example.appdevproject.audioList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.appdevproject.R
import com.example.appdevproject.data.AudioData

class AudioAdapter(private val audios: List<AudioData>) :
    RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    class AudioViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val audioTitle: TextView = itemView.findViewById(R.id.audio_title)
        private var currentAudioData: AudioData? = null

        fun bind(audio: AudioData) {
            Log.d("debug", "Binding")
            currentAudioData = audio

            audioTitle.text = audio.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        Log.d("debug", "HERE")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.audio_item, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        Log.d("debug", "HERE")
        holder.bind(audios[position])
    }

    override fun getItemCount(): Int {
        Log.d("debug", "HERE")
        return audios.size
    }
}
