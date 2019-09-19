package com.n8ify.charon.presentation.item.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.n8ify.charon.R
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.presentation.item.adapter.GuessResultAdapter
import kotlinx.android.synthetic.main.dialog_result.view.*
import java.util.concurrent.LinkedBlockingQueue

class ResultDialogFragment : DialogFragment() {

    val TAG = ResultDialogFragment::class.java.simpleName

    companion object {

        fun newInstance(
            guessItemResult: LinkedBlockingQueue<Pair<Item, Boolean>>,
            guessItemSize: Int
        ): ResultDialogFragment {
            return ResultDialogFragment().apply {
                this@apply.guessItemResult = guessItemResult
                this@apply.isCancelable = false
            }
        }

    }

    private lateinit var guessItemResult: LinkedBlockingQueue<Pair<Item, Boolean>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val resultSize = guessItemResult.size
        val correctCount = guessItemResult.count { it.second }
        view?.let {
            it.tv_result.text =
                getString(R.string.timeup)
            it.rv_result.apply {
                this@apply.layoutManager = LinearLayoutManager(context)
                this@apply.adapter = GuessResultAdapter.newInstance(guessItemResult)
            }
            it.btn_back.setOnClickListener {
                if(activity is ResultCallback){
                    (activity as ResultCallback).onBackClick()
                }
            }
            Handler().postDelayed({
                activity?.runOnUiThread {
                    it.btn_back.isEnabled = true
                }
            }, 3000)
        }
    }

    override fun onStart() {
        super.onStart().also {
            dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    private fun resultMessage(resultSize: Int, correctCount: Int): String {
        return when (if (correctCount != 0 && resultSize != 0) {
            correctCount.div(resultSize) * 100
        } else {
            0
        }) {
            0 -> getString(R.string.result_message_feedback_f)
            in 1..10 -> getString(R.string.result_message_feedback_d)
            in 11..20 -> getString(R.string.result_message_feedback_c)
            in 21..60 -> getString(R.string.result_message_feedback_c_plus)
            in 61..70 -> getString(R.string.result_message_feedback_b)
            in 71..80 -> getString(R.string.result_message_feedback_b_plus)
            in 81..90 -> getString(R.string.result_message_feedback_a)
            in 91..99 -> getString(R.string.result_message_feedback_a_plus)
            100 -> getString(R.string.result_message_feedback_a_plus_plus)
            else -> ""
        }
    }


    interface ResultCallback {

        fun onBackClick()

    }
}