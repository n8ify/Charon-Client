package com.n8ify.charon.presentation.main.viewholder

import android.view.View
import com.n8ify.charon.model.entity.Category
import com.n8ify.charon.presentation._base.activity.BaseActivity
import com.n8ify.charon.presentation._base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_category.view.*
import timber.log.Timber

class CategoryViewHolder(private val view : View) : BaseViewHolder(view) {

    fun bind(category : Category){
        view.tv_category.text = category.name
        view.cv_category.setOnClickListener {
            if((context as BaseActivity) is CategoryContext){
                (context as CategoryContext).onCategoryClick(category)
                Timber.i(view.id.toString())
            }
        }
    }

    interface CategoryContext {
        fun onCategoryClick(category : Category)
    }

}