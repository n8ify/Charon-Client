package com.n8ify.charon.presentation.item.fragment

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.n8ify.charon.R
import com.n8ify.charon.constant.CommonConstant
import timber.log.Timber

class PrepareFragment : DialogFragment() {

    val TAG = PrepareFragment::class.java.simpleName

    val countDownTimer =
        object : CountDownTimer(CommonConstant.DEFAULT_PREPARE_TIME_MILLISECOND, CommonConstant.MILLISECOND) {

            override fun onFinish() {
                Timber.i("Prepare time up!")
                postActionFunction.invoke(this@PrepareFragment)
            }

            override fun onTick(millisUntilFinished: Long) {
                val secondLeft = millisUntilFinished / CommonConstant.MILLISECOND
                Timber.i("Prepare time left : %d", secondLeft)
                if (secondLeft != 0L) {
                    view?.findViewById<TextView>(R.id.tv_prepare_countdown)?.text = secondLeft.toString()
                }
            }

        }

    companion object {

        fun newInstance(postActionFunction: (DialogFragment) -> Unit): PrepareFragment {
            return PrepareFragment().apply {
                this@apply.postActionFunction = postActionFunction
            }
        }

    }

    lateinit var postActionFunction: (DialogFragment) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_prepare, container, false).also {
            isCancelable = false
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        countDownTimer.start()
    }
}