package com.example.appdevproject

import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdevproject.audioList.AudioAdapter
import com.example.appdevproject.data.AudioData
import com.example.appdevproject.data.DataSource

class MainActivity : AppCompatActivity() {
    private var serviceConnection: ServiceConnection = new ServiceConnection() {
        overri
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        val intent = Intent(this, AudioService::class.java)
        startService(intent)

        // RECYCLER
        Log.d("debug", "HERE")
        val audioList = DataSource(this.resources).getAudioList()
        val recyclerView: RecyclerView = findViewById(R.id.rv_audio_list)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = AudioAdapter(audioList, object: AudioAdapter.ActionListener {
            override fun onClicked(audioData: AudioData) {
                Toast.makeText(this@MainActivity, "${audioData.title} clicked",
                    Toast.LENGTH_SHORT).show()
            }

            override fun OnSliderChange(audioData: AudioData, value: Int) {
                Toast.makeText(this@MainActivity, "Slider #${audioData.title} -> $value",
                    Toast.LENGTH_LONG).show()
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
        }

        return super.onOptionsItemSelected(item)
    }
}