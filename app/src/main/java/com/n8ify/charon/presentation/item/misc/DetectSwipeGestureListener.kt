package com.n8ify.charon.presentation.item.misc

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity

class DetectSwipeGestureListener : GestureDetector.SimpleOnGestureListener() {

    companion object {

        lateinit var detectSwipeGestureListener : DetectSwipeGestureListener

        fun getInstance(listener : OnDirectionChangeListener) : DetectSwipeGestureListener {
            if(!::detectSwipeGestureListener.isInitialized){
                detectSwipeGestureListener = DetectSwipeGestureListener().apply {
                    this@apply.listener = listener
                }
            }
            return detectSwipeGestureListener
        }

    }

    private lateinit var listener : OnDirectionChangeListener

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {

        T(e1, e2)?.let {

            val MAX_X_DISTANCE = 1000
            val MIN_X_DISTANCE = 100

            val MAX_Y_DISTANCE = 1000
            val MIN_Y_DISTANCE = 100

            val deltaX = it.first.x - it.second.x
            val absDeltaX = Math.abs(it.first.x - it.second.x)
            val deltaY = it.first.y - it.second.y
            val absDeltaY = Math.abs(it.first.y - it.second.y)

            if (absDeltaX.toInt() in MIN_X_DISTANCE..MAX_X_DISTANCE) {
                if(deltaX < 0){
                    listener.onRight()
                } else {
                    listener.onLeft()
                }
            }
            if(absDeltaY.toInt() in MIN_Y_DISTANCE..MAX_Y_DISTANCE){
                if(deltaY < 0){
                    listener.onDown()
                } else {
                    listener.onUp()
                }
            }
        }

        return true
    }

    private fun <E1, E2> T(e1: E1?, e2: E2?): Pair<E1, E2>? {
        return if (e1 != null && e2 != null) {
            (e1 to e2)
        } else {
            null
        }
    }

    interface OnDirectionChangeListener {

        fun onUp() : Unit
        fun onDown() : Unit
        fun onLeft() : Unit
        fun onRight() : Unit

    }

}