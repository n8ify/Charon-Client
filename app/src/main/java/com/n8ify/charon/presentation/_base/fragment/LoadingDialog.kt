package com.n8ify.charon.presentation._base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.n8ify.charon.R

class LoadingDialog : DialogFragment() {

    val TAG = LoadingDialog::class.java.simpleName

    companion object {

        private lateinit var loadingDialog : LoadingDialog

        fun getInstance() : LoadingDialog {

            if(!::loadingDialog.isInitialized){
                loadingDialog = LoadingDialog()
            }

            return loadingDialog
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.loading_dialog, container, true).also {
            isCancelable = false
        }
    }


}