package com.ifpr.androidapptemplate.ui.campaing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CampaignViewModel : ViewModel(){

    private val _text = MutableLiveData<String>().apply {
        value = "Criar sua Campanha"
    }

    val text: LiveData<String> = _text

}