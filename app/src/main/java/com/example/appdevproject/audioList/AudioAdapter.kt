package com.example.appdevproject.audioList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.appdevproject.R
import com.example.appdevproject.data.AudioData

class AudioAdapter(private val audios: List<AudioData>, private val actionListener: ActionListener) :
    RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    private val listener: ActionListener = actionListener

    interface ActionListener {
        fun onClicked(audio: AudioData)
        fun OnSliderChange(audio: AudioData, value: Int)
    }

    class AudioViewHolder(itemView: View, private val listener: ActionListener) :
        RecyclerView.ViewHolder(itemView) {

        private val audioTitle: TextView = itemView.findViewById(R.id.audio_title)
        private var currentAudioData: AudioData? = null

        init {
            itemView.setOnClickListener {
                currentAudioData?.let {
                    listener.onClicked(it)
                }
            }
        }

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
        return AudioViewHolder(view, listener)
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
