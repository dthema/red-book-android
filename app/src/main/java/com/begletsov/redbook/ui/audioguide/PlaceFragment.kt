package com.begletsov.redbook.ui.audioguide

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MEDIA_ROUTER_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRouter
import android.media.MediaRouter.CALLBACK_FLAG_UNFILTERED_EVENTS
import android.media.MediaRouter.ROUTE_TYPE_USER
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
import androidx.navigation.fragment.findNavController
import com.begletsov.redbook.Data
import com.begletsov.redbook.R
import com.begletsov.redbook.databinding.FragmentPlaceBinding
import com.begletsov.redbook.models.Place
import com.begletsov.redbook.ui.utils.GlideApp
import com.google.android.material.slider.Slider
import java.io.File
import java.util.ArrayList
import java.util.UUID


class PlaceFragment : Fragment() {

    private var _binding: FragmentPlaceBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioManager: AudioManager

    private var index = 0
    private val places = ArrayList<Place>()

    private val handler = Handler(Looper.myLooper()!!)
    private val sliderCallback = Runnable {
        sliderProgressUpdater()
    }

    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent.let {
                setPlace(index)
                binding.placeLoading.visibility = GONE
                unregisterBroadCastReceiver()
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

        val categoryId = UUID.fromString(requireArguments().getString("categoryId"))
        val placeId = UUID.fromString(requireArguments().getString("id"))
        val category = Data.categories.first { it.id == categoryId }
        places.addAll(category.places)
        index = category.places.indexOfFirst { it.id == placeId }

        binding.placeMediaPlay.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                binding.placeMediaPlay.setImageResource(R.drawable.baseline_play_arrow)
            } else {
                mediaPlayer.start()
                binding.placeMediaPlay.setImageResource(R.drawable.baseline_pause)
            }
        }

        binding.placeMediaForward.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition + 10000)
        }

        binding.placeMediaReplay.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition - 10000)
        }

        binding.placeMediaVolumeUp.setOnClickListener {
            if (audioManager.isStreamMute(AudioManager.STREAM_MUSIC))
                binding.placeMediaVolumeMute.setImageResource(R.drawable.baseline_volume)
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
        }

        binding.placeMediaVolumeDown.setOnClickListener {
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
        }

        if (audioManager.isStreamMute(AudioManager.FLAG_PLAY_SOUND)) {
            binding.placeMediaVolumeMute.setImageResource(R.drawable.baseline_volume_off)
        } else {
            binding.placeMediaVolumeMute.setImageResource(R.drawable.baseline_volume)
        }
        binding.placeMediaVolumeMute.setOnClickListener {
            if (audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
                audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_PLAY_SOUND)
                binding.placeMediaVolumeMute.setImageResource(R.drawable.baseline_volume)
            } else {
                audioManager.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_PLAY_SOUND)
                binding.placeMediaVolumeMute.setImageResource(R.drawable.baseline_volume_off)
            }
        }

        binding.placeVolumeSlider.valueTo = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        binding.placeVolumeSlider.value = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        binding.placeVolumeSlider.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                if (audioManager.isStreamMute(AudioManager.STREAM_MUSIC))
                    binding.placeMediaVolumeMute.setImageResource(R.drawable.baseline_volume)
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value.toInt(), AudioManager.FLAG_PLAY_SOUND)
            }
        }

        val mediaRouter = requireContext().getSystemService(MEDIA_ROUTER_SERVICE) as MediaRouter
        mediaRouter.addCallback(ROUTE_TYPE_USER, callback, CALLBACK_FLAG_UNFILTERED_EVENTS)
        binding.placeBack.setOnClickListener { navController.navigate(R.id.action_navigation_place_pop) }
        binding.placePrev.setOnClickListener {
            mediaPlayer.stop()
            setPlace(if (index == 0) places.size - 1 else index - 1)
        }
        binding.placeNext.setOnClickListener {
            mediaPlayer.stop()
            setPlace(if (index == places.size - 1) 0 else index + 1)
        }

        setPlace(index)

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.let {
            mediaPlayer.stop()
        }
        handler.removeCallbacks(sliderCallback)
    }

    private fun registerBroadCastReceiver() {
        requireContext().registerReceiver(downloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun unregisterBroadCastReceiver() {
        requireContext().unregisterReceiver(downloadReceiver)
    }

    private fun setPlace(placeIndex: Int) {
        val place = places[placeIndex]
        handler.removeCallbacks(sliderCallback)

        val uri = downloadAudio(place)
        if (uri == null) {
            binding.placeLoading.visibility = VISIBLE
            return
        }

        mediaPlayer = MediaPlayer.create(context, uri)
        binding.placeLoading.visibility = GONE
        sliderProgressUpdater()

        binding.placeTitle.text = place.description.name
        if (place.description.imagePath.isNotEmpty())
            GlideApp.with(requireContext())
                .load(place.description.imagePath)
                .into(binding.placeImage)

        binding.placeCurrentMediaTime.text = getTimeString(0)
        binding.placeCurrentMaxMediaTime.text = getTimeString(mediaPlayer.duration)
        binding.placeMediaSlider.valueTo = mediaPlayer.duration.toFloat()

        binding.placeMediaSlider.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) mediaPlayer.seekTo(value.toInt())
        }

        binding.placePrevName.text = places[if (placeIndex == 0) places.size - 1 else placeIndex - 1]
                                        .description.name
        binding.placeNextName.text = places[if (placeIndex == places.size - 1) 0 else placeIndex + 1]
            .description.name

        index = placeIndex
    }

    private fun downloadAudio(place: Place): Uri? {
        val filename = "podcast-name.mp3"
        val file = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS)}/$filename")
        if (file.exists())
            return file.toUri()

        val request = DownloadManager.Request(Uri.parse(place.description.audioFilePath))
        request.setDescription("Downloading podcast")
        request.setMimeType("audio/MP3")
        request.setTitle("Podcast ${place.description.name}")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PODCASTS, filename)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        val manager = getSystemService(requireContext(), DownloadManager::class.java)!!
        manager.enqueue(request)
        registerBroadCastReceiver()

        return null
    }

    private fun sliderProgressUpdater() {
        updateSlider()
        handler.postDelayed(sliderCallback, 500)
    }

    private fun updateSlider() {
        binding.placeMediaSlider.value = mediaPlayer.currentPosition.toFloat()
        binding.placeCurrentMediaTime.text = getTimeString(mediaPlayer.currentPosition)
    }

    private fun getTimeString(millis: Int): String {
        val buf = StringBuffer()
        val hours = (millis / (1000 * 60 * 60))
        val minutes = (millis % (1000 * 60 * 60) / (1000 * 60))
        val seconds = (millis % (1000 * 60 * 60) % (1000 * 60) / 1000)
        if (hours != 0) {
            buf.append(String.format("%02d", hours))
                .append(":")
        }
        buf.append(String.format("%02d", minutes))
            .append(":")
            .append(String.format("%02d", seconds))
        return buf.toString()
    }

    private val callback = object : MediaRouter.Callback() {
        override fun onRouteSelected(p0: MediaRouter?, p1: Int, p2: MediaRouter.RouteInfo?) { }

        override fun onRouteUnselected(p0: MediaRouter?, p1: Int, p2: MediaRouter.RouteInfo?) { }

        override fun onRouteAdded(p0: MediaRouter?, p1: MediaRouter.RouteInfo?) { }

        override fun onRouteRemoved(p0: MediaRouter?, p1: MediaRouter.RouteInfo?) { }

        override fun onRouteChanged(p0: MediaRouter?, p1: MediaRouter.RouteInfo?) { }

        override fun onRouteGrouped(p0: MediaRouter?, p1: MediaRouter.RouteInfo?, p2: MediaRouter.RouteGroup?, p3: Int) { }

        override fun onRouteUngrouped(p0: MediaRouter?, p1: MediaRouter.RouteInfo?, p2: MediaRouter.RouteGroup?) { }

        override fun onRouteVolumeChanged(p0: MediaRouter?, info: MediaRouter.RouteInfo?) {
            binding.placeVolumeSlider.value = info?.volume?.toFloat() ?: binding.placeVolumeSlider.value
        }
    }
}