package com.n8ify.charon.presentation.item.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import com.n8ify.charon.R
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.presentation._base.activity.BaseActivity
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel
import com.n8ify.charon.presentation.item.misc.DetectSwipeGestureListener
import com.n8ify.charon.presentation.item.viewmodel.ItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class GuessActivity : BaseActivity(), DetectSwipeGestureListener.OnDirectionChangeListener {

    private val itemViewModel: ItemViewModel by viewModel()

    private val gestureCompat by lazy {
        GestureDetectorCompat(this@GuessActivity, DetectSwipeGestureListener.getInstance(this@GuessActivity))
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)
    }

    override fun initObserver(vararg baseViewModels: BaseViewModel) {
        super.initObserver(itemViewModel)
        itemViewModel.getItem(intent.extras.getInt("categoryId")).also {
            itemViewModel.guessQueue.observe(this, androidx.lifecycle.Observer {
                println(it.remainingCapacity())
            })
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event).also {
            gestureCompat.onTouchEvent(event)
        }
    }

    override fun onUp() {
        println("Up!")
        with(itemViewModel.guessQueue.value?.peek()) {
            this@with?.second?.or(true)
        }
    }

    override fun onRight() {
        println("Right!")
        with(itemViewModel.guessQueue.value?.peek()) {
            this@with?.second?.or(true)
        }
    }

    override fun onDown() {
        println("Down!")
        with(itemViewModel.guessQueue.value?.peek()) {
            this@with?.second?.and(false)
        }
    }

    override fun onLeft() {
        println("Left!")
        with(itemViewModel.guessQueue.value?.peek()) {
            this@with?.second?.and(false)
        }
    }

}
