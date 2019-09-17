package com.n8ify.charon.presentation.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.n8ify.charon.R
import com.n8ify.charon.presentation._base.activity.BaseActivity
import com.n8ify.charon.presentation._base.viewmodel.CategoryViewModel
import com.n8ify.charon.presentation.item.activity.GuessActivity
import com.n8ify.charon.presentation.main.adapter.CategoryAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

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

        initProgressObserver(categoryViewModel)

    }

}
