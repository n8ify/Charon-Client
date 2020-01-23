package com.n8ify.charon.presentation._base.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

abstract class BaseViewModel(application : Application) : AndroidViewModel(application) {
    val remoteConfig by lazy { FirebaseRemoteConfig.getInstance() }
    val isOnProgress by lazy { MutableLiveData<Boolean>().apply { this@apply.value = false } }
}