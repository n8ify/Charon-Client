package com.n8ify.charon.presentation.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.n8ify.charon.R
import com.n8ify.charon.model.entity.Category
import com.n8ify.charon.presentation._base.activity.BaseActivity
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel
import com.n8ify.charon.presentation._base.viewmodel.CategoryViewModel
import com.n8ify.charon.presentation.item.activity.GuessActivity
import com.n8ify.charon.presentation.main.adapter.CategoryAdapter
import com.n8ify.charon.presentation.main.viewholder.CategoryViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), CategoryViewHolder.CategoryContext {

    private val categoryViewModel: CategoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categoryViewModel.categories.observe(this, Observer { categories ->
            rcv_category.apply {
                this@apply.layoutManager = GridLayoutManager(applicationContext, 2)
                this@apply.adapter = CategoryAdapter.getInstance(categories)
            }

        }).also { categoryViewModel.getTotalCategories() }

    }

    override fun initObserver(vararg baseViewModels: BaseViewModel) {
        super.initObserver(categoryViewModel)
    }

    override fun onCategoryClick(category: Category) {
        val intent = Intent(this@MainActivity, GuessActivity::class.java).apply {
            this@apply.putExtra("categoryId", category.id)
        }
        startActivity(intent)
    }
}
