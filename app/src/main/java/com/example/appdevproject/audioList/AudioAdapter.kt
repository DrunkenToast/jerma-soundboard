package com.example.appdevproject.audioList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.appdevproject.R
import com.example.appdevproject.data.AudioData
// TODO combine db cursor, notifyDataSetChanged
class AudioAdapter(private val audios: List<AudioData>, private val actionListener: ActionListener) :
    RecyclerView.Adapter<AudioAdapter.AudioViewHolder>(){

    private val listener: ActionListener = actionListener

    interface ActionListener {
        fun onClicked(audio: AudioData)
        fun onLongClicked(audio: AudioData)
    }


    class AudioViewHolder(itemView: View, private val listener: ActionListener) :
        RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.audio_title)
        private val button: ImageButton = itemView.findViewById(R.id.button)

        private var currentAudioData: AudioData? = null
        init {

            button.setOnClickListener {
                currentAudioData?.let {
                    listener.onClicked(it)

                }
            }

            button.setOnLongClickListener() {
                currentAudioData?.let {
                    listener.onLongClicked(it)
                }
                true
            }
        }

        fun bind(audio: AudioData) {
            currentAudioData = audio

            title.text = audio.title
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.audio_item, parent, false)
        return AudioViewHolder(view, listener)
    }


    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(audios[position])
    }

    override fun getItemCount(): Int {
        return audios.size
    }


}
