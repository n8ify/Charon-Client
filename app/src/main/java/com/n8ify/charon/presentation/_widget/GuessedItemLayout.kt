package com.n8ify.charon.presentation._widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.n8ify.charon.R
import com.n8ify.charon.model.entity.Item
import kotlinx.android.synthetic.main.view_guessed_item.view.*

class GuessedItemLayout @JvmOverloads constructor(context: Context?, private val item : Item, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr) {


    init {
        LayoutInflater.from(context).inflate(R.layout.view_guessed_item, this).run {
            this@run.tv_value.text = this@GuessedItemLayout.item.value
        }
    }



}