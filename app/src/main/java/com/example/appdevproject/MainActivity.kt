package com.example.appdevproject

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdevproject.api.APIHandler
import com.example.appdevproject.audioList.AudioAdapter
import com.example.appdevproject.data.DataSource
import com.example.appdevproject.db.AudioViewModel
import com.example.appdevproject.db.DBHelper

class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var mService: AudioService
    private var mBound: Boolean = false
    private lateinit var audioViewModel: AudioViewModel

    private fun serviceLoadAudioDB() {
        val audioList = audioViewModel.AudioDBList.value
        if (audioList != null)
            for (audio in audioList)
                mService.load(audio.id, audio.src)
    }

    private var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as AudioService.AudioServiceBinder
            mService = binder.getService()
            mBound = true

            setupPermissions()

            Toast.makeText(this@MainActivity, "Audio service connected",
                Toast.LENGTH_SHORT).show()

            for (audio in DataSource().getAudioList()) {
                mService.load(audio.id, audio.src)
            }

            if (checkPermission()) {
                serviceLoadAudioDB()
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Apply theme on startup
        var sf = PreferenceManager.getDefaultSharedPreferences(this)
        Util.applyPreferencedTheme(sf, this)

        setSupportActionBar(findViewById(R.id.toolbar))

        setupTwitchStreamViews()

        // Start Audio Service
        val intent = Intent(this, AudioService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        audioViewModel = ViewModelProvider(this).get(AudioViewModel::class.java)
        audioViewModel.loadAudio(this)

        // RECYCLERVIEW
        val audioList = DataSource().getAudioList()
        val recyclerView: RecyclerView = findViewById(R.id.rv_audio_list)
        val layoutManager = GridLayoutManager(this, 3)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        // Override actions from audio item
        recyclerView.adapter = AudioAdapter(audioList, object: AudioAdapter.ActionListener {
            override fun onClicked(audioID: Int) {
                if (mBound) {
                    mService.play(audioID, 1.0F)
                }
            }

            override fun onLongClicked(audioID: Int) {
                val db = DBHelper(this@MainActivity, null)
                db.deleteAudio(audioID)
                audioViewModel.loadAudio(this@MainActivity)
            }
        })

        // Update recyclerview | Observe changes to audio list
        audioViewModel.AudioDBList.observe(this@MainActivity) {
            (recyclerView.adapter as AudioAdapter).setAudioDB(it)
        }
    }

    private fun setupTwitchStreamViews() {
        // Update stream status
        val streamStatusText = findViewById<TextView>(R.id.stream_status_text)
        val api = APIHandler(this)
        api.streamStatus.observe(this) {
            streamStatusText.text = it
        }

        // Visit stream button
        val twitchOpenIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.twitch_url)))
        val visitStreamButton = findViewById<Button>(R.id.open_stream_but)
        visitStreamButton.setOnClickListener {
            startActivity(twitchOpenIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.popup_menu, menu)
        return true
    }

    private val addAudioLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        audioViewModel.loadAudio(this@MainActivity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.addaudio -> {
                val intent = Intent(this, AddAudioActivity::class.java)
                addAudioLauncher.launch(intent)
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
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // denied
                }
                else {
                    serviceLoadAudioDB()
                }
            }
        }
    }

    private fun setupPermissions() {
        if (checkPermission()) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    fun checkPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        Log.d("TAG", "Permission" + (permission != PackageManager.PERMISSION_GRANTED))

        return permission == PackageManager.PERMISSION_GRANTED
    }
}