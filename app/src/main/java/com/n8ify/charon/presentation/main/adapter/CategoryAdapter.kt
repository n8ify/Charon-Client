package com.n8ify.charon.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n8ify.charon.R
import com.n8ify.charon.model.entity.Category
import com.n8ify.charon.presentation.main.viewholder.CategoryViewHolder

class CategoryAdapter(private val categories : List<Category>) : RecyclerView.Adapter<CategoryViewHolder>() {

    companion object {

        private lateinit var adapter : CategoryAdapter

        fun getInstance(categories : List<Category>) : CategoryAdapter {

            if(!::adapter.isInitialized){
                adapter = CategoryAdapter(categories)
            }

            return adapter

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }
}