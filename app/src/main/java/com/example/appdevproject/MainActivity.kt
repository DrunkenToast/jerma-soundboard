package com.example.appdevproject

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdevproject.api.APIHandler
import com.example.appdevproject.audioList.AudioAdapter
import com.example.appdevproject.data.AudioDataDB
import com.example.appdevproject.data.DataSource
import com.example.appdevproject.db.AudioViewModel
import com.example.appdevproject.db.DBHelper

class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 100
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

            if (checkPermission()) {
                val audiolist = viewModel.AudioDBList.value
                if (audiolist != null)
                    serviceLoadAudioDB(audiolist)
            }


        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
        }
    }
    private fun serviceLoadAudioDB(audioDataDB: MutableList<AudioDataDB>) {
        for (audio in audioDataDB)
            mService.load(audio.id, audio.src)

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPermissions()





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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }



    private fun setupPermissions() {
        if (checkPermission()) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
            // DO STUFF
        }
    }

    private fun checkPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)

        return permission != PackageManager.PERMISSION_GRANTED
    }
}