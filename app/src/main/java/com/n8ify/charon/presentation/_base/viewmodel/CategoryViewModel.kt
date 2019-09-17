package com.n8ify.charon.presentation._base.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.n8ify.charon.data.repository.CategoryRepository
import com.n8ify.charon.model.entity.Category
import com.n8ify.charon.model.misc.UseCaseResult
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class CategoryViewModel(
    private val categoryRepository: CategoryRepository,
    application: Application
) : BaseViewModel(application), CoroutineScope {

    val categories = MutableLiveData<List<Category>>()

    fun getTotalCategories() {
        isOnProgress.value = true
        launch {
            when (val useCaseResult = withContext(Dispatchers.IO) { categoryRepository.getTotalCategories() }) {
                is UseCaseResult.Success -> {
                    Timber.i("Categories = %s", useCaseResult.result.toString())
                    categories.value = useCaseResult.result.data
                }
                is UseCaseResult.Error -> {
                    Timber.e(useCaseResult.t)
                }
            }
            isOnProgress.value = false
        }
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.plus(Job())

    override fun onCleared() {
        super.onCleared()
    }


}