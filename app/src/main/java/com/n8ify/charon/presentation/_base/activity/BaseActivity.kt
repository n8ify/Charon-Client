package com.n8ify.charon.presentation._base.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.n8ify.charon.constant.CommonConstant
import com.n8ify.charon.presentation._base.fragment.LoadingDialog
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {


    private val progressDialog by lazy { LoadingDialog.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserver()
    }

    val pref by lazy { getSharedPreferences( CommonConstant.APP_PREFERENCE_NAME, Context.MODE_PRIVATE) }

    open fun initView() {}
    open fun initObserver(vararg baseViewModels: BaseViewModel) {
        baseViewModels.forEach { viewModel ->
            viewModel.isOnProgress.observe(this, Observer { isOnProgress ->
                when (isOnProgress) {
                    true -> {
                        Timber.i("Loading...")
                        showProgressDialog()
                    }
                    else -> {
                        Timber.i("Load Finished...")
                        dismissProgressDialog()
                    }
                }
            })
        }
    }

    protected fun showProgressDialog() {
        if (supportFragmentManager.findFragmentByTag(progressDialog.TAG) == null) {
            supportFragmentManager
                .beginTransaction()
                .add(progressDialog, progressDialog.TAG)
                .commit()
        }
    }

    protected fun dismissProgressDialog() {
        if (supportFragmentManager.findFragmentByTag(progressDialog.TAG) != null) {
            supportFragmentManager
                .beginTransaction()
                .remove(progressDialog)
                .commit()
        }
    }


}