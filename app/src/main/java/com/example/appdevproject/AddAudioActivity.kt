package com.example.appdevproject

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.appdevproject.db.AudioContentProvider
import com.example.appdevproject.db.AudioViewModel
import com.example.appdevproject.db.DBHelper

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
            val t = titleText.text.toString()
            val f = filenameText.text.toString()

            if (f.isNotEmpty() && t.isNotEmpty()) {
                val db = DBHelper(this, null)

                val values = ContentValues()
                values.put(DBHelper.COL_TITLE, t)
                values.put(DBHelper.COL_SRC, f)

                contentResolver.insert(
                    AudioContentProvider.BASE_CONTENT_URI,
                    values
                )
//                db.addAudio(t, f)
                ViewModelProvider(this).get(AudioViewModel::class.java).loadAudio(this)
                finish()
            }
        }
    }

    var getResult = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        result ->
        run {
            filenameText.text = result.toString()
        }
    }

    private fun showFileDialog() {
        getResult.launch(arrayOf("audio/*"))
    }
}