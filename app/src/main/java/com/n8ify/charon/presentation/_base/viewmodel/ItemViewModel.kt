package com.n8ify.charon.presentation._base.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.MutableLiveData
import com.n8ify.charon.constant.RemoteConfigConstant
import com.n8ify.charon.data.repository.ItemRepository
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.model.misc.UseCaseResult
import com.n8ify.charon.presentation.BaseApplication
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.LinkedBlockingQueue
import kotlin.coroutines.CoroutineContext

class ItemViewModel(private val itemRepository: ItemRepository, application: Application) : BaseViewModel(application),
    CoroutineScope {

    val guessQueue by lazy { MutableLiveData<LinkedBlockingQueue<Item>>().apply { this.value = LinkedBlockingQueue() } }
    val guessQueueResult by lazy { LinkedBlockingQueue<Pair<Item, Boolean>>() }
    var guessQueueSize: Int = 0

    private val sensorManager = (getApplication<BaseApplication>().getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    private val accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private val accelerationListener = object : SensorEventListener {

        var actualTime : Long = System.currentTimeMillis()

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {

            event?.let {

            if(event.timestamp - actualTime > 2000000000){
                return@let
            }
                accX.value = it.values[0]
                accY.value = it.values[1]
                accZ.value = it.values[2]

                actualTime = event.timestamp

                println("y = ${accY.value}")

            }
        }
    }

    val accX = MutableLiveData<Float>()
    val accY = MutableLiveData<Float>()
    val accZ = MutableLiveData<Float>()

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

    fun initialSensor() {
        sensorManager.registerListener(accelerationListener, accelerationSensor, SensorManager.SENSOR_DELAY_NORMAL)

    }

    fun terminateSensor(){
        sensorManager.unregisterListener(accelerationListener)
    }

    override fun onCleared() {
        super.onCleared()
        terminateSensor()

    }
}