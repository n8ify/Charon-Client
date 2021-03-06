package com.n8ify.charon.presentation._base.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.n8ify.charon.R
import com.n8ify.charon.presentation._base.fragment.LoadingDialog
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel
import kotlinx.android.synthetic.main.activity_guess.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {


    private val progressDialog by lazy { LoadingDialog.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserver()
    }


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