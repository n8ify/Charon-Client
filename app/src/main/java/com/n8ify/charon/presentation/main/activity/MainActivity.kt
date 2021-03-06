package com.n8ify.charon.presentation.main.activity

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.crashlytics.android.Crashlytics
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.n8ify.charon.R
import com.n8ify.charon.constant.RemoteConfigConstant
import com.n8ify.charon.data.room.CentralDatabase
import com.n8ify.charon.model.entity.Category
import com.n8ify.charon.presentation._base.activity.BaseActivity
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel
import com.n8ify.charon.presentation._base.viewmodel.CategoryViewModel
import com.n8ify.charon.presentation.item.activity.GuessActivity
import com.n8ify.charon.presentation.main.adapter.CategoryAdapter
import com.n8ify.charon.presentation.main.viewholder.CategoryViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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
