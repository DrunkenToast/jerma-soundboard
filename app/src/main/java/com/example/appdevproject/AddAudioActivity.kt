package com.example.appdevproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.example.appdevproject.db.DBHelper
import java.io.File
import java.nio.file.Files

class AddAudioActivity : AppCompatActivity() {
    lateinit var filenameText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_audio)

        var addAudioButton = findViewById<Button>(R.id.add_audio_but);
        var filePickerButton = findViewById<Button>(R.id.pick_file_but);
        var titleText = findViewById<EditText>(R.id.editNewAudioName)

        filenameText = findViewById(R.id.file_name)

        filePickerButton.setOnClickListener {
            showFileDialog()
        }

        addAudioButton.setOnClickListener {
            /*TODO check if file exists*/
            /*var f = File(filenameText.text.toString())
            if (f.exists()) {*/
            var t = titleText.text.toString()
            var f = filenameText.text.toString()

            if (f.isNotEmpty() && t.isNotEmpty()) {
                val db = DBHelper(this, null)

                db.addAudio(t, f)
                finish()
            }
        }
    }

    var getResult = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        result ->
        run {
            filenameText.text = result.path
        }
    }

    fun showFileDialog() {

        getResult.launch(arrayOf("audio/*"))
    }
}