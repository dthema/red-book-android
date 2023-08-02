package com.begletsov.redbook.ui.moodguide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MoodguideViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Экран Гида по настроению"
    }
    val text: LiveData<String> = _text
}