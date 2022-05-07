package com.example.appdevproject

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdevproject.api.APIHandler
import com.example.appdevproject.audioList.AudioAdapter
import com.example.appdevproject.data.AudioData
import com.example.appdevproject.data.DataSource
import com.example.appdevproject.db.AudioViewModel
import com.example.appdevproject.db.DBHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var mService: AudioService
    private var mBound: Boolean = false
    private lateinit var viewModel: AudioViewModel

    private var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as AudioService.AudioServiceBinder
            mService = binder.getService()
            mBound = true

            Toast.makeText(this@MainActivity, "Audio service connected",
                Toast.LENGTH_SHORT).show()

            for (audio in DataSource().getAudioList()) {
                mService.load(audio.id, audio.src)
            }

            /*viewModel.AudioDBList.observe(this@MainActivity) {
                Log.d("RON", "Observed")
                for (audio in it)
                    mService.load(audio.id, audio.src)
            }*/
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        setSupportActionBar(findViewById(R.id.toolbar))

        viewModel = ViewModelProvider(this).get(AudioViewModel::class.java)
        viewModel.loadAudio(this)

        val intent = Intent(this, AudioService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)



        // RECYCLER
        val audioList = DataSource().getAudioList()
        val recyclerView: RecyclerView = findViewById(R.id.rv_audio_list)
        val layoutManager = GridLayoutManager(this, 3)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = AudioAdapter(audioList, object: AudioAdapter.ActionListener {
            override fun onClicked(audioID: Int) {
                if (mBound) {
                    mService.play(audioID, 1.0F)
                }
            }

            override fun onLongClicked(audioID: Int) {
                val db = DBHelper(this@MainActivity, null)
                db.deleteAudio(audioID)
                viewModel.loadAudio(this@MainActivity)
            }
        })

        viewModel.AudioDBList.observe(this@MainActivity) {
            Log.d("RON", "Observed")
            (recyclerView.adapter as AudioAdapter).setAudioDB(it)
        }

        val streamStatusText = findViewById<TextView>(R.id.stream_status_text)
        val api = APIHandler(this)
        api.streamStatus.observe(this) {
            streamStatusText.text = it
        }
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