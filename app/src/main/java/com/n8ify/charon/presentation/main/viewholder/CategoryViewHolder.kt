package com.n8ify.charon.presentation.main.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.n8ify.charon.model.entity.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {

    fun bind(category : Category){
        view.tv_category.text = category.name
    }

}