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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdevproject.api.APIHandler
import com.example.appdevproject.audioList.AudioAdapter
import com.example.appdevproject.audioList.AudioAdapter.AudioItem
import com.example.appdevproject.data.DataSource
import com.example.appdevproject.db.AudioViewModel
import com.google.android.material.appbar.AppBarLayout

class MainFragment : Fragment() {
    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var mService: AudioService
    private var mBound: Boolean = false
    private lateinit var audioViewModel: AudioViewModel
    private lateinit var streamStatusText: TextView
    private lateinit var visitStreamButton: Button
    private var streamSurface: AppBarLayout? = null

    private fun serviceLoadAudioDB() {
        val audioList = audioViewModel.AudioDBList.value
        if (audioList != null)
            for (audio in audioList)
                mService.load(audio.id, audio.src)
    }

    private var serviceConnection: ServiceConnection = object :
        ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as AudioService.AudioServiceBinder
            mService = binder.getService()
            mBound = true

            setupPermissions()

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

        // Apply theme on startup
        val sf = PreferenceManager.getDefaultSharedPreferences(activity)
        activity?.let { Util.applyPreferencedTheme(sf, it.baseContext) }

        // Start Audio Service
        val intent = Intent(activity, AudioService::class.java)
        activity?.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        // oh no !! anyway.
        audioViewModel = activity?.let { ViewModelProvider(it).get(AudioViewModel::class.java) }!!
        context?.let { audioViewModel.loadAudio(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        streamStatusText = view.findViewById(R.id.stream_status_text)
        visitStreamButton = view.findViewById(R.id.open_stream_but)
        streamSurface = view.findViewById(R.id.stream_status_frame)

        setupTwitchStreamViews()

        Log.d("NICE RON", "on view created")

        // RECYCLERVIEW
        val audioList = DataSource().getAudioList()
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_audio_list)
        val layoutManager = GridLayoutManager(activity, 3)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        // Override actions from audio item
        recyclerView.adapter = AudioAdapter(audioList, object: AudioAdapter.ActionListener {
            override fun onClicked(audioID: Int) {
                if (mBound) {
                    mService.play(audioID, 1.0F)
                }
            }

            override fun onLongClicked(audioItem: AudioItem) {
                if ((activity as MainActivity).isTablet()) {
                    val bundle = Bundle()
                    bundle.putInt("audioItem", audioItem.id)

                    val frag = AudioDetailFragment()
                    frag.arguments = bundle
                    (activity as MainActivity).supportFragmentManager.commit {
                        replace(R.id.details_fragment_container, frag)
                        setReorderingAllowed(true)
                    }
                }
                else {
                    val intent = Intent(
                        activity,
                        AudioDetailActivity::class.java
                    )
                    intent.putExtra("audioItem", audioItem.id)
                    addAudioLauncher.launch(intent)
                }
            }
        })

        // Update recyclerview | Observe changes to audio list
        Log.d("MAIN FRAG", "Setting observer")
        audioViewModel.AudioDBList.observe(viewLifecycleOwner) {
            Log.d("MAIN FRAG", "Audio updated, updating recycler view")
            (recyclerView.adapter as AudioAdapter).setAudioDB(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    val addAudioLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        context?.let { it1 -> audioViewModel.loadAudio(it1) }
    }

    private fun setupTwitchStreamViews() {
        // Update stream status
        val api = context?.let { APIHandler(it) }
        api?.streamStatus?.observe(viewLifecycleOwner) {
            streamStatusText.text = it
        }

        // Visit stream button
        val twitchOpenIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.twitch_url)))
        visitStreamButton.setOnClickListener {
            startActivity(twitchOpenIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activity?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("NICE RON", "test!!!")
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
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
            }
        }
    }

    fun checkPermission(): Boolean {
        val permission = context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        Log.d("TAG", "Permission" + (permission != PackageManager.PERMISSION_GRANTED))

        return permission == PackageManager.PERMISSION_GRANTED
    }
}