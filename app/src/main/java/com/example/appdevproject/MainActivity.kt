package com.example.appdevproject

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdevproject.audioList.AudioAdapter
import com.example.appdevproject.data.AudioData
import com.example.appdevproject.data.DataSource

class MainActivity : AppCompatActivity() {
    private lateinit var mService: AudioService
    private var mBound: Boolean = false

    private var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as AudioService.AudioServiceBinder
            mService = binder.getService()
            mBound = true

            Log.d("TAG", "SET")
            Toast.makeText(this@MainActivity, "Connected",
                Toast.LENGTH_SHORT).show()
            for (audio in DataSource(this@MainActivity.resources).getAudioList()) {
                mService.load(audio.id, audio.src)
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        val intent = Intent(this, AudioService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        // RECYCLER
        Log.d("debug", "HERE")
        val audioList = DataSource(this.resources).getAudioList()
        val recyclerView: RecyclerView = findViewById(R.id.rv_audio_list)
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = AudioAdapter(audioList, object: AudioAdapter.ActionListener {
            override fun onClicked(audioData: AudioData) {
                if (mBound) {
                    mService.play(audioData.id, 1.0F)
                }
            }

            override fun onLongClicked(audio: AudioData) {
                Toast.makeText(this@MainActivity, "Long press!",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.popup_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.addaudio -> {
                val intent = Intent(this, AddAudioActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}