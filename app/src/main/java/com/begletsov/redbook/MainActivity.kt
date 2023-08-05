package com.begletsov.redbook

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.begletsov.redbook.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), RecognitionListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var speech: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        navController = findNavController(R.id.nav_host_fragment_activity_main)
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

        requirePermission(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_PERMISSION)

        binding.likeButton.isSelected = false
        binding.likeButton.setOnClickListener {
            if (it.isSelected)
                binding.likeButton.setImageResource(R.drawable.baseline_favorite_border)
            else
                binding.likeButton.setImageResource(R.drawable.baseline_favorite)
            it.isSelected = !it.isSelected
        }

        initSpeechRecognize()
    }

    private fun initSpeechRecognize() {
        speech = SpeechRecognizer.createSpeechRecognizer(this)
        speech.setRecognitionListener(this)
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
            "ru")
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, "5000")
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, "5000")
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)

        binding.recognizeLayout.recognizeProgressBar.max = 10

        binding.recognizeStartButton.setOnClickListener {
            val havePermission = requirePermission(listOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_PERMISSION)
            if (!havePermission) return@setOnClickListener
            clearRecognizeLayout()
            binding.recognizeLayout.root.visibility = VISIBLE
            speech.startListening(recognizerIntent)
        }

        binding.recognizeLayout.recognizeBack.setOnClickListener {
            binding.recognizeLayout.root.visibility = GONE
            speech.stopListening()
            speech.cancel()
        }
    }

    private fun requirePermission(permissions: List<String>, code: Int): Boolean {
        val neededPermissions = ArrayList<String>()
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                neededPermissions.add(permission)
            }
        }

        if (neededPermissions.isEmpty()) return true

        val array = Array(neededPermissions.size) { i: Int -> neededPermissions[i] }
        ActivityCompat.requestPermissions(
            this,
            array,
            code
        )
        return false
    }

    override fun onBeginningOfSpeech() { }

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() { }

    override fun onError(errorCode: Int) {
        binding.recognizeLayout.recognizeUserText.text = getString(R.string.recognize_error)
        setErrorRecognizeButton()
    }

    override fun onEvent(arg0: Int, arg1: Bundle?) {}

    override fun onPartialResults(arg0: Bundle?) {}

    override fun onReadyForSpeech(arg0: Bundle?) {}

    override fun onResults(results: Bundle) {
        val result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)!![0]
        binding.recognizeLayout.recognizeUserText.text = result
        binding.recognizeLayout.recognizeAnswer.visibility = VISIBLE
        binding.recognizeLayout.recognizeProgressBar.visibility = GONE
        if (result.contains("место")) {
            val placeResult = result.split("место ")[1].split(" ")[0].lowercase()
            val place = Data.categories.firstOrNull { it.places.any { it.description.name.split(" ")[0].lowercase() == placeResult } }
                ?.places?.firstOrNull { it.description.name.split(" ")[0].lowercase() == placeResult }
            if (place == null) {
                binding.recognizeLayout.recognizeAnswerText.text = getString(R.string.recognize_error_place)
                setErrorRecognizeButton()
                return
            }
            val bundle = Bundle()
            bundle.putString("id", place.id.toString())
            bundle.putString("categoryId", place.categoryId.toString())
            setSuccessRecognizeButton(R.id.navigation_place, bundle)
        } else if (result.contains("категория")) {
            val categoryResult = result.split("категория ")[1].split(" ")[0].lowercase()
            val category = Data.categories.firstOrNull { it.name.split(" ")[0].lowercase() == categoryResult }
            if (category == null) {
                binding.recognizeLayout.recognizeAnswerText.text = getString(R.string.recognize_error_place)
                setErrorRecognizeButton()
                return
            }
            val bundle = Bundle()
            bundle.putString("id", category.id.toString())
            setSuccessRecognizeButton(R.id.navigation_choose_place, bundle)
        } else {
            binding.recognizeLayout.recognizeAnswerText.text = getString(R.string.recognize_tip)
            setErrorRecognizeButton()
        }
    }

    override fun onRmsChanged(rmsdB: Float) {
        binding.recognizeLayout.recognizeProgressBar.progress = rmsdB.toInt()
    }

    private fun setErrorRecognizeButton() {
        binding.recognizeLayout.recognizeButton.visibility = VISIBLE
        binding.recognizeLayout.recognizeButton.text = "Повторить"
        binding.recognizeLayout.recognizeButton.setOnClickListener {
            speech.startListening(recognizerIntent)
            clearRecognizeLayout()
        }
    }

    private fun setSuccessRecognizeButton(id: Int, bundle: Bundle) {
        binding.recognizeLayout.recognizeButton.visibility = VISIBLE
        binding.recognizeLayout.recognizeButton.text = "Перейти"
        binding.recognizeLayout.recognizeButton.setOnClickListener {
            binding.recognizeLayout.root.visibility = GONE
            navController.navigate(id, bundle)
        }
    }

    private fun clearRecognizeLayout() {
        binding.recognizeLayout.recognizeUserText.text = getString(R.string.recognize_text_in_progress)
        binding.recognizeLayout.recognizeProgressBar.visibility = VISIBLE
        binding.recognizeLayout.recognizeAnswer.visibility = INVISIBLE
        binding.recognizeLayout.recognizeButton.visibility = INVISIBLE
    }

    companion object {
        private const val REQUEST_WRITE_PERMISSION = 100
        private const val REQUEST_RECORD_PERMISSION = 101
    }
}