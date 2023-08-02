package com.begletsov.redbook

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.begletsov.redbook.databinding.ActivityMainBinding
import org.jetbrains.annotations.Nullable

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_audioguide, R.id.navigation_excursion, R.id.navigation_moodguide
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//
//        navController.navigate(R.id.navigation_audioguide)
//
//        binding.mainAudioGuideButton.setOnClickListener { navController.navigate(R.id.navigation_audioguide) }
//        binding.mainMoodGuideButton.setOnClickListener { navController.navigate(R.id.navigation_moodguide) }
//        binding.mainExcursionGuideButton.setOnClickListener { navController.navigate(R.id.navigation_excursion) }
//
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            if (destination.id != R.id.navigation_audioguide ||
//                destination.id != R.id.navigation_excursion ||
//                destination.id != R.id.navigation_moodguide) {
//                binding.optionMenu.visibility = VISIBLE
//            } else {
//                binding.optionMenu.visibility = GONE
//            }
//        }
    }
}