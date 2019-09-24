package com.n8ify.charon.presentation._base.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.n8ify.charon.constant.RemoteConfigConstant
import com.n8ify.charon.data.repository.ItemRepository
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.model.misc.UseCaseResult
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.LinkedBlockingQueue
import kotlin.coroutines.CoroutineContext

class ItemViewModel(private val itemRepository: ItemRepository, application: Application) : BaseViewModel(application),
    CoroutineScope {

    val guessQueue by lazy { MutableLiveData<LinkedBlockingQueue<Item>>().apply { this.value = LinkedBlockingQueue() } }
    val guessQueueResult by lazy { LinkedBlockingQueue<Pair<Item, Boolean>>() }
    var guessQueueSize : Int = 0


    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.plus(job)

    fun getItem(categoryId: Int) {
        isOnProgress.value = true
        launch {
            when (val useCase = withContext(Dispatchers.IO) {
                itemRepository.getItem(
                    categoryId
                    , remoteConfig.getLong(RemoteConfigConstant.DEFAULT_ITEM_AMOUNT).toInt()
                    , remoteConfig.getString(RemoteConfigConstant.DEFAULT_ITEM_LIST_POLICY)
                )
            }) {
                is UseCaseResult.Success -> {
                    // Note : Paring guess item and default un-guess flag.
                    this@ItemViewModel.guessQueue.value = LinkedBlockingQueue(useCase.result.data)
//                    guessQueueSize = this@ItemViewModel.guessQueue.value?.size?:-1
                }
                is UseCaseResult.Error -> {
                    Timber.e(useCase.t)
                }
            }
            isOnProgress.value = false
        }
    }

    fun correct(item: Item) {
        guessQueueResult.add(item to true)
    }

    fun skip(item: Item) {
        guessQueueResult.add(item to false)
    }

}