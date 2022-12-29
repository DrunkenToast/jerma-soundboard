package com.example.appdevproject

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Audio
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appdevproject.audioList.AudioAdapter
import com.example.appdevproject.data.AudioDataDB
import com.example.appdevproject.db.AudioContentProvider
import com.example.appdevproject.db.AudioViewModel
import com.example.appdevproject.db.DBHelper

// Groetjes, Xander xoxo
class AudioDetailFragment : Fragment() {
    private var audioItem: AudioDataDB? = null
    private lateinit var audioViewModel: AudioViewModel

    private lateinit var audioTitleText: TextView
    private lateinit var audioSourceText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // oh no !! anyway
        audioViewModel = activity?.let { ViewModelProvider(it)[AudioViewModel::class.java] }!!
        var id: Int? = null

        arguments?.let {
            id = it.getInt("audioItem")
        }

        if (id == null) return

        val cursor = context?.contentResolver?.query(
            Uri.withAppendedPath(
                AudioContentProvider.BASE_CONTENT_URI,
                AudioContentProvider.AUDIO_PATH + "/$id"
            ), null, null, null, null, null
        )
        try {
            if (cursor?.moveToFirst() == true) {
                audioItem = AudioDataDB(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_SRC)),
                )
            }
        }
        catch (e: Exception) {
            audioItem = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_audio_detail,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("NICE RON", "Add audio view created")
        super.onViewCreated(view, savedInstanceState)

        audioTitleText = view.findViewById(R.id.audio_title)
        audioSourceText = view.findViewById(R.id.audio_source)

        audioItem?.let {
            Log.d("NICE RON", "AUDIO TEXT EXISTS")
            audioTitleText.text = "#${it.id} ${it.title}"
            audioSourceText.text = it.src
        }

        view.findViewById<Button>(R.id.audioDeleteButton).setOnClickListener {
            if (audioItem == null) return@setOnClickListener

            context?.contentResolver?.delete(
                Uri.withAppendedPath(
                    AudioContentProvider.BASE_CONTENT_URI,
                    AudioContentProvider.AUDIO_PATH + "/" + audioItem!!.id
                )
                , null, null
            )

            if (activity is MainActivity) { // empty the fragment
                val fragMan = activity?.supportFragmentManager
                val fragTransaction = fragMan?.beginTransaction()
                fragTransaction?.remove(this)?.commit()

                // Reload audio in viewmodel, viewmodels are per activity
                audioViewModel = activity?.let { ViewModelProvider(it).get(AudioViewModel::class.java) }!!
                context?.let { audioViewModel.loadAudio(it) }
            }
            else {
                // We finish the activity, main activity has to handle the finish
                // and then reload audio
                activity?.finish()
            }
        }

    }
}