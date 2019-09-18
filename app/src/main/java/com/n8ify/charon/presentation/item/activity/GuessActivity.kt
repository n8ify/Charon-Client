package com.n8ify.charon.presentation.item.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import com.n8ify.charon.R
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.presentation._base.activity.BaseActivity
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel
import com.n8ify.charon.presentation.item.fragment.PrepareFragment
import com.n8ify.charon.presentation.item.misc.DetectSwipeGestureListener
import com.n8ify.charon.presentation.item.viewmodel.ItemViewModel
import kotlinx.android.synthetic.main.activity_guess.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class GuessActivity : BaseActivity(), DetectSwipeGestureListener.OnDirectionChangeListener {

    private val itemViewModel: ItemViewModel by viewModel()

    private val gestureCompat by lazy {
        GestureDetectorCompat(this@GuessActivity, DetectSwipeGestureListener.getInstance(this@GuessActivity))
    }

    val countDownTimer by lazy {
        object : CountDownTimer(itemViewModel.roundTime, 1000) {
            override fun onFinish() {
                Toast.makeText(this@GuessActivity, "Timeout!", Toast.LENGTH_LONG).show()
                // TODO : Show result.
            }

            override fun onTick(millisUntilFinished: Long) {
                val secondLeft = millisUntilFinished / 1000
                Timber.i("Second left : %d ", secondLeft)
                tv_timer.text = secondLeft.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)

        itemViewModel.guessQueue.observe(this, androidx.lifecycle.Observer {
            Timber.i("Remaining : %s", it.size)
            Timber.i("Left(s) : %s", it)
        }).also { itemViewModel.getItem(intent.extras.getInt("categoryId")) }

    }

    private fun prepareAndStart(){

        val prepareFragment = PrepareFragment.newInstance {
                supportFragmentManager.beginTransaction().remove(it).commit()
                countDownTimer.start()
        }

        supportFragmentManager
            .beginTransaction()
            .add(prepareFragment, prepareFragment.TAG)
            .commit()
    }

    override fun initObserver(vararg baseViewModels: BaseViewModel) {

        super.initObserver(itemViewModel)
        itemViewModel.isOnProgress.observe(this, androidx.lifecycle.Observer {
            if(!it){ prepareAndStart() }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event).also {
            gestureCompat.onTouchEvent(event)
        }
    }

    override fun onUp() {
        itemViewModel.guessQueue.value?.remove()?.let {
            println("Correct! ${itemViewModel.guessQueue.value}")
            itemViewModel.correct(it)
        }
    }

    override fun onDown() {
        itemViewModel.guessQueue.value?.remove()?.let {
            println("Skip! ${itemViewModel.guessQueue.value}")
            itemViewModel.skip(it)
        }
    }

    override fun onRight() {
        println("Right!")
    }


    override fun onLeft() {
        println("Left!")
    }

}
