package com.ifpr.androidapptemplate.ui.campaing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CampaignViewViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Ver Campanha"
    }

    val text: LiveData<String> = _text

}