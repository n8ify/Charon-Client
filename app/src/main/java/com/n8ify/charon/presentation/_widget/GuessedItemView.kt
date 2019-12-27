package com.n8ify.charon.presentation._widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
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
//                this@run.tv_value.textSize = when {
//                    this@with.length < 12 -> 56.toSp()
//                    this@with.length < 18 -> 48.toSp()
//                    this@with.length < 24 -> 36.toSp()
//                    this@with.length < 32 -> 24.toSp()
//                    else -> 64.toSp()
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

}