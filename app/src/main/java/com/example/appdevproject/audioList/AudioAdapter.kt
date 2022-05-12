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
import com.example.appdevproject.data.AudioDataDB

// TODO combine db cursor, notifyDataSetChanged
class AudioAdapter(private val defaultAudioList: List<AudioData>, private val actionListener: ActionListener) :
    RecyclerView.Adapter<AudioAdapter.AudioViewHolder>(){

    private val listener: ActionListener = actionListener
    private var audioList: MutableList<AudioItem> = mutableListOf()

    interface ActionListener {
        fun onClicked(audioID: Int)
        fun onLongClicked(audioID: Int)
    }

    fun setAudioDB(audioDB: List<AudioDataDB>) {
        audioList.clear()
        for (audio in defaultAudioList)
            audioList.add(AudioItem(audio.id, audio.title, false))

        for (audio in audioDB) {
            Log.d("TEST", audio.title)
            audioList.add(AudioItem(audio.id, audio.title, true))
        }

        notifyDataSetChanged()
        Log.d("TEST", "set audio")
    }

    class AudioViewHolder(itemView: View, private val listener: ActionListener) :
        RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.audio_title)
        private val button: ImageButton = itemView.findViewById(R.id.button)

        private var currentAudioData: AudioItem? = null
        init {

            button.setOnClickListener {
                currentAudioData?.let {
                    listener.onClicked(it.id)

                }
            }

            button.setOnLongClickListener() {
                currentAudioData?.let {
                    if (it.custom)
                        listener.onLongClicked(it.id)
                }
                true
            }
        }

        fun bind(audio: AudioItem) {
            currentAudioData = audio
            title.text = audio.title

            if (audio.custom) // TODO change image
                title.text = audio.title + " DB"
        }
    }

    init {
        for (audio in defaultAudioList)
            audioList.add(AudioItem(audio.id, audio.title, false))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.audio_item, parent, false)
        return AudioViewHolder(view, listener)
    }



    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(audioList[position])
    }

    override fun getItemCount(): Int {
        return audioList.size
    }

    data class AudioItem(
        val id: Int,
        val title: String,
        val custom: Boolean
    )
}
