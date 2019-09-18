package com.n8ify.charon.presentation.item.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.n8ify.charon.data.repository.ItemRepository
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.model.misc.UseCaseResult
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.LinkedBlockingQueue
import kotlin.coroutines.CoroutineContext

class ItemViewModel(private val itemRepository: ItemRepository, application: Application) : BaseViewModel(application),
    CoroutineScope {

    val guessQueue by lazy { MutableLiveData<LinkedBlockingQueue<Pair<Item, Boolean>>>() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.plus(Job())

    fun getItem(categoryId: Int) {
        isOnProgress.value = true
        launch {
            when (val useCase = withContext(Dispatchers.IO) { itemRepository.getItem(categoryId) }) {
                is UseCaseResult.Success -> {
                    // Note : Paring guess item and default un-guess flag.
                    this@ItemViewModel.guessQueue.value = LinkedBlockingQueue<Pair<Item, Boolean>>().apply {
                        useCase.result.data.forEach {
                            this.add(it to false)
                        }
                    }
                }
                is UseCaseResult.Error -> {
                    Timber.e(useCase.t)
                }
            }
            isOnProgress.value = false
        }
    }

}