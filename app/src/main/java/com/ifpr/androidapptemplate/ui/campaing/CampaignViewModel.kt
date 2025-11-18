package com.ifpr.androidapptemplate.ui.campaing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CampaignViewModel : ViewModel(){

    private val _text = MutableLiveData<String>().apply {
        value = "This is a Campaign Fragment"
    }

    val text: LiveData<String> = _text

}