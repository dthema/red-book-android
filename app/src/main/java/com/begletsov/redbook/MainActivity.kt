package com.begletsov.redbook

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.begletsov.redbook.databinding.ActivityMainBinding
import org.jetbrains.annotations.Nullable
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.navigation_audioguide)

        binding.mainAudioGuideButton.setOnClickListener { navController.navigate(R.id.navigation_audioguide) }
        binding.mainMoodGuideButton.setOnClickListener { navController.navigate(R.id.navigation_moodguide) }
        binding.mainExcursionGuideButton.setOnClickListener { navController.navigate(R.id.navigation_excursion) }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.likeButton.isSelected = false
            binding.likeButton.setImageResource(R.drawable.baseline_favorite_border)

            if (destination.id == R.id.navigation_audioguide ||
                destination.id == R.id.navigation_excursion ||
                destination.id == R.id.navigation_moodguide) {
                binding.likeButton.visibility = GONE
            } else {
                binding.likeButton.visibility = VISIBLE
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_RECORD_PERMISSION
            )
        }

        binding.likeButton.isSelected = false
        binding.likeButton.setOnClickListener {
            if (it.isSelected)
                binding.likeButton.setImageResource(R.drawable.baseline_favorite_border)
            else
                binding.likeButton.setImageResource(R.drawable.baseline_favorite)
            it.isSelected = !it.isSelected
        }
    }

    companion object {
        private const val REQUEST_RECORD_PERMISSION = 101
    }
}