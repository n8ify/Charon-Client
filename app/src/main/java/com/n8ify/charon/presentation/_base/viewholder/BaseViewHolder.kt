package com.n8ify.charon.presentation._base.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    val context by lazy { view.context }

}