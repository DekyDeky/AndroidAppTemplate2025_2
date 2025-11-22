package com.ifpr.androidapptemplate.ui.sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SheetViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Sua Ficha"
    }

    val text: LiveData<String> = _text
}