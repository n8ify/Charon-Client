package com.n8ify.charon.presentation._base.viewholder

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val context = view.context

    fun getString(resId : Int) : String = context.getString(resId)

}