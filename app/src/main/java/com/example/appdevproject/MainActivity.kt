package com.example.appdevproject

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.appdevproject.db.AudioViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var audioViewModel: AudioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isTablet()) {
            Log.d("NICE RON", "Is a tablet!")
            setContentView(R.layout.activity_main_tablet)
        }
        else {
            Log.d("NICE RON", "Is a phone!")
            setContentView(R.layout.activity_main_phone)
        }

        supportFragmentManager.commit {
            replace(R.id.main_fragment_container, MainFragment())
            setReorderingAllowed(true)
        }

        audioViewModel = ViewModelProvider(this)[AudioViewModel::class.java]

        setSupportActionBar(findViewById(R.id.toolbar))

        // open audio
        findViewById<View>(R.id.audio_fab).setOnClickListener {
            val intent = Intent(this, AddAudioActivity::class.java)
            activityLauncher.launch(intent)
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
                activityLauncher.launch(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        Log.d("TEST", "HERE")
        audioViewModel.loadAudio(this@MainActivity)
    }

    fun isTablet(): Boolean {
        return ((resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE)
    }
}