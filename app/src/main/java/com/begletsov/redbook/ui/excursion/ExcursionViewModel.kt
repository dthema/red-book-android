package com.begletsov.redbook.ui.excursion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExcursionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Экран Экскурсий"
    }
    val text: LiveData<String> = _text
}