package com.n8ify.charon.presentation.item.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import com.n8ify.charon.R
import com.n8ify.charon.presentation._base.activity.BaseActivity
import com.n8ify.charon.presentation.item.misc.DetectSwipeGestureListener
import com.n8ify.charon.presentation.item.viewmodel.ItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GuessActivity : BaseActivity(), DetectSwipeGestureListener.OnDirectionChangeListener {

    private val itemViewModel : ItemViewModel by viewModel()

    private val gestureCompat by lazy {
        GestureDetectorCompat(this@GuessActivity, DetectSwipeGestureListener.getInstance(this@GuessActivity))
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)

        initProgressObserver(itemViewModel)

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event).also {
            gestureCompat.onTouchEvent(event)
        }
    }

    override fun onUp() {
        println("Up!")
    }

    override fun onRight() {
        println("Right!")
    }

    override fun onDown() {
        println("Down!")
    }

    override fun onLeft() {
        println("Left!")
    }
}
