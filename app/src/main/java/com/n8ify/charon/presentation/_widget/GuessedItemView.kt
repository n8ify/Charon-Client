package com.n8ify.charon.presentation._widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import com.n8ify.charon.R
import com.n8ify.charon.model.entity.Item
import kotlinx.android.synthetic.main.view_guessed_item.view.*
import timber.log.Timber

class GuessedItemView @JvmOverloads constructor(
    context: Context?,
    private val item: Item,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {


    init {
        LayoutInflater.from(context).inflate(R.layout.view_guessed_item, this).run {

            with(this@GuessedItemView.item.value) {
                this@run.tv_value.text = this@with
//                this@run.tv_value.textSize = when(this@with.length.div(4)) {
//                    3 -> 56.toSp()
//                    4 -> 48.toSp()
//                    6 -> 36.toSp()
//                    8 -> 24.toSp()
//                    else -> 56.toSp()
//                }
            }
        }
    }

    fun Int.toSp(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this@toSp.toFloat(),
            context.resources.displayMetrics
        ).also {
            Timber.d("Input size .. $this, Outputsize .. $it")
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { event ->
            //            var dX : Float = 0F
//            var dY : Float = 0F
            when(event.action){
//                MotionEvent.ACTION_DOWN -> {
//                    dX = this.x - event.rawX
//                    dY = this.y - event.rawY
//                }
                MotionEvent.ACTION_MOVE -> {
            this.animate().setInterpolator(AccelerateInterpolator())
                .translationY(event.rawY)
                .setDuration(250)
                .start()
                }
            }
            return false
        }

        return super.onTouchEvent(event)
    }
}