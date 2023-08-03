package com.begletsov.redbook.ui.audioguide

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.begletsov.redbook.R
import com.begletsov.redbook.databinding.FragmentPlaceBinding
import com.begletsov.redbook.models.Description
import com.begletsov.redbook.models.Geopoint
import com.begletsov.redbook.models.Place
import com.begletsov.redbook.ui.utils.GlideApp
import com.google.android.material.slider.Slider
import java.io.File
import java.util.UUID


class PlaceFragment : Fragment() {

    private var _binding: FragmentPlaceBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioManager: AudioManager

    private val place = Place(
        UUID.fromString("fbf4ab56-8abb-47a2-929a-c68a69df4c0d"),
        Description("Let’s Rock", "", "", "https://p1.zoon.ru/c/7/51ff64a2a0f3024a1a000015_5c6a764b312e4.jpg", "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"),
        UUID.randomUUID(),
        Geopoint(0.0, 0.0)
    )

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent.let {
                setPlace(place)
                binding.placeLoading.visibility = GONE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val navController = findNavController()
        audioManager = getSystemService(requireContext(), AudioManager::class.java)!!

        binding.placeMediaPlay.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                binding.placeMediaPlay.setImageResource(R.drawable.baseline_play_arrow)
            } else {
                mediaPlayer.start()
                binding.placeMediaPlay.setImageResource(R.drawable.baseline_pause)
                sliderProgressUpdater()
            }
        }

        binding.placeMediaForward.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition + 10000)
            updateSlider()
        }

        binding.placeMediaReplay.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition - 10000)
            updateSlider()
        }

        binding.placeMediaVolumeUp.setOnClickListener {
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
        }

        binding.placeMediaVolumeDown.setOnClickListener {
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
        }

        binding.placeBack.setOnClickListener { navController.navigate(R.id.action_navigation_place_pop) }

        setPlace(place)

        return root
    }

    private fun registerBroadCastReceiver() {
        requireContext().registerReceiver(
            receiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private fun setPlace(place: Place) {
        val uri = downloadAudio(place)
        if (uri == null) {
            binding.placeLoading.visibility = VISIBLE
            return
        }
        binding.placeLoading.visibility = GONE
        binding.placeTitle.text = place.description.name
        if (place.description.imagePath.isNotEmpty())
            GlideApp.with(requireContext())
                .load(place.description.imagePath)
                .into(binding.placeImage)

        mediaPlayer = MediaPlayer.create(context, uri)
        binding.placeCurrentMediaTime.text = getTimeString(0)
        binding.placeCurrentMaxMediaTime.text = getTimeString(mediaPlayer.duration.toLong())
        binding.placeMediaSlider.valueTo = mediaPlayer.duration.toFloat()

        binding.placeMediaSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) { }

            override fun onStopTrackingTouch(slider: Slider) {
                mediaPlayer.seekTo(slider.value.toInt())
            }
        })
    }

    private fun downloadAudio(place: Place): Uri? {
        val filename = "${place.id}.mp3"
        val file = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS)}/$filename")
        if (file.exists())
            return file.toUri()

        val request = DownloadManager.Request(Uri.parse(place.description.audioFilePath))
        request.setDescription("Downloading")
        request.setMimeType("audio/MP3")
        request.setTitle("File :")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PODCASTS, filename)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        val manager = getSystemService(requireContext(), DownloadManager::class.java)!!
        manager.enqueue(request)
        registerBroadCastReceiver()

        return null
    }

    private fun sliderProgressUpdater() {
        updateSlider()
        if (mediaPlayer.isPlaying) {
            val notification = Runnable { sliderProgressUpdater() }
            handler.postDelayed(notification, 100)
        } else
            binding.placeMediaPlay.setImageResource(R.drawable.baseline_play_arrow)
    }

    private fun updateSlider() {
        binding.placeMediaSlider.value = mediaPlayer.currentPosition.toFloat()
        binding.placeCurrentMediaTime.text = getTimeString(mediaPlayer.currentPosition.toLong())
    }

    private fun getTimeString(millis: Long): String {
        val buf = StringBuffer()
        val hours = (millis / (1000 * 60 * 60)).toInt()
        val minutes = (millis % (1000 * 60 * 60) / (1000 * 60)).toInt()
        val seconds = (millis % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        if (hours != 0) {
            buf.append(String.format("%02d", hours))
                .append(":")
        }
        buf.append(String.format("%02d", minutes))
            .append(":")
            .append(String.format("%02d", seconds))
        return buf.toString()
    }

    companion object {
        private val handler = Handler(Looper.myLooper()!!)
    }
}