package com.example.appdevproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class AddAudioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_audio)

        var filePickerButton = findViewById<Button>(R.id.pick_file_but);
        var filenameText = findViewById<TextView>(R.id.file_name)

        filePickerButton.setOnClickListener {
            showFileDialog()
        }
    }

    fun showFileDialog() {
        var getResult = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            result -> {
                result.path
            }
        }
        getResult.launch(arrayOf("audio/*"))
    }
}