package com.n8ify.charon.presentation.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.n8ify.charon.R
import com.n8ify.charon.constant.RemoteConfigConstant
import com.n8ify.charon.model.entity.Category
import com.n8ify.charon.presentation._base.activity.BaseActivity
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel
import com.n8ify.charon.presentation._base.viewmodel.CategoryViewModel
import com.n8ify.charon.presentation.item.activity.GuessActivity
import com.n8ify.charon.presentation.main.adapter.CategoryAdapter
import com.n8ify.charon.presentation.main.viewholder.CategoryViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : BaseActivity(), CategoryViewHolder.CategoryContext {

    private val categoryViewModel: CategoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Step [1] : Load Remote Configuration.
        FirebaseRemoteConfig.getInstance().run {
            this@run.setDefaults(R.xml.remote_config_defaults)
            this@run.fetch(0)
                .addOnCompleteListener(this@MainActivity, OnCompleteListener {
                    if (it.isSuccessful) {
                        Timber.i("Remote Configuration is Completely Loaded! .. ${it.result}")
                        this@run.activateFetched()
                        Timber.i("${RemoteConfigConstant.DEFAULT_ITEM_AMOUNT} : ${this@run.getLong(RemoteConfigConstant.DEFAULT_ITEM_AMOUNT)}")
                        Timber.i(
                            "${RemoteConfigConstant.DEFAULT_ITEM_LIST_POLICY} : ${this@run.getString(
                                RemoteConfigConstant.DEFAULT_ITEM_LIST_POLICY
                            )}"
                        )
                        pref.edit().run {
                            putLong(RemoteConfigConstant.DEFAULT_ITEM_AMOUNT, getLong(RemoteConfigConstant.DEFAULT_ITEM_AMOUNT)).apply()
                            putLong(RemoteConfigConstant.DEFAULT_ITEM_LIST_POLICY, getLong(RemoteConfigConstant.DEFAULT_ITEM_LIST_POLICY)).apply()
                            putLong(RemoteConfigConstant.IA_HURRY_OR_DEDUCT_TIME_MIN, getLong(RemoteConfigConstant.IA_HURRY_OR_DEDUCT_TIME_MIN)).apply()
                            putLong(RemoteConfigConstant.IA_HURRY_OR_DEDUCT_TIME_MAX, getLong(RemoteConfigConstant.IA_HURRY_OR_DEDUCT_TIME_MAX)).apply()
                            putLong(RemoteConfigConstant.IA_HURRY_AND_INCREASE_TIME_MIN, getLong(RemoteConfigConstant.IA_HURRY_AND_INCREASE_TIME_MIN)).apply()
                            putLong(RemoteConfigConstant.IA_HURRY_AND_INCREASE_TIME_MAX, getLong(RemoteConfigConstant.IA_HURRY_AND_INCREASE_TIME_MAX)).apply()
                            putLong(RemoteConfigConstant.IA_HURRY_OR_END_MIN, getLong(RemoteConfigConstant.IA_HURRY_OR_END_MIN)).apply()
                            putLong(RemoteConfigConstant.IA_HURRY_OR_END_MAX, getLong(RemoteConfigConstant.IA_HURRY_OR_END_MAX)).apply()
                            putLong(RemoteConfigConstant.IA_RANDOM_RATE, getLong(RemoteConfigConstant.IA_RANDOM_RATE)).apply()
                            putLong(RemoteConfigConstant.IA_HURRY_OR_DEDUCT_WEIGHT, getLong(RemoteConfigConstant.IA_HURRY_OR_DEDUCT_WEIGHT)).apply()
                            putLong(RemoteConfigConstant.IA_HURRY_AND_INCREASE_WEIGHT, getLong(RemoteConfigConstant.IA_HURRY_AND_INCREASE_WEIGHT)).apply()
                            putLong(RemoteConfigConstant.IA_HURRY_OR_END_WEIGHT, getLong(RemoteConfigConstant.IA_HURRY_OR_END_WEIGHT)).apply()
                            pref.all.forEach {
                                Timber.i("Config .. ${it.key} -> ${it.value}")
                            }
                        }
                    } else {
                        Timber.e(it.exception)
                    }
                })
        }

//        GlobalScope.launch(Dispatchers.Main) {
//            CentralDatabase.getInstance().historyDao().getHistoryAndResult().forEach {
//                Timber.i("\n- - - - - - - - - - - - - - - \n")
//                Timber.i("History Id : ${it.id}")
//                Timber.i("Category Name : ${it.categoryName}")
//                Timber.i("Corrected Result(s) : ${it.results.filter { result -> result.itemResult }}")
//                Timber.i("Skipped Result(s) : ${it.results.filter { result -> !result.itemResult }}")
//                Timber.i("\n- - - - - - - - - - - - - - - \n")
//            }
//        }


    }

    override fun initObserver(vararg baseViewModels: BaseViewModel) {
        super.initObserver(categoryViewModel)

        categoryViewModel.categories.observe(this, Observer { categories ->
            rcv_category.apply {
                this@apply.layoutManager = GridLayoutManager(applicationContext, 3)
                this@apply.adapter = CategoryAdapter.getInstance(categories)
            }

        }).also { categoryViewModel.getTotalCategories() }

    }

    override fun onCategoryClick(category: Category) {
        AlertDialog.Builder(this@MainActivity)
            .setTitle(R.string.option_timeCount_title)
            .setItems(R.array.time_count_option) { _, which ->
                val timeCount = resources.getStringArray(R.array.time_count_option).map { it.toInt() }[which]
                val intent = Intent(this@MainActivity, GuessActivity::class.java).apply {
                    this@apply.putExtra("categoryId", category.id)
                    this@apply.putExtra("categoryName", category.name)
                    this@apply.putExtra("timeCount", timeCount)
                }
                startActivity(intent)
            }
            .setCancelable(false)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create().also {
                it.show()
            }

    }

}
