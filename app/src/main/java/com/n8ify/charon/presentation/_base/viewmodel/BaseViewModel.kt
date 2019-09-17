package com.n8ify.charon.presentation._base.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.n8ify.charon.presentation.BaseApplication

abstract class BaseViewModel(application : Application) : AndroidViewModel(application) {
    val isOnProgress by lazy { MutableLiveData<Boolean>().apply { this@apply.value = false } }
}