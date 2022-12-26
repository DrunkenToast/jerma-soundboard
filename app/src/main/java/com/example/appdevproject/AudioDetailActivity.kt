package com.example.appdevproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit

class AudioDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_detail)

        val id = intent.getIntExtra("audioItem", 0)

        val bundle = Bundle()
        bundle.putInt("audioItem", id)

        val frag = AudioDetailFragment()
        frag.arguments = bundle

        supportFragmentManager.commit {
            replace(R.id.fragment_container, frag)
            setReorderingAllowed(true)
        }
    }
}