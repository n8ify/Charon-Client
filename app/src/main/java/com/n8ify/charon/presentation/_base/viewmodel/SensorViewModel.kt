package com.n8ify.charon.presentation._base.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.n8ify.charon.presentation.BaseApplication

class SensorViewModel(application : Application): BaseViewModel(application) {


    val accX = MutableLiveData<Float>()
    val accY = MutableLiveData<Float>()
    val accZ = MutableLiveData<Float>()

    private val sensorManager = (getApplication<BaseApplication>().getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    private val accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    private val accelerationListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {

            event?.let {
                accX.value = it.values[0]
                accY.value = it.values[1]
                accZ.value = it.values[2]

                println("y = ${accY.value}")

            }
        }
    }

    fun initialSensor() {
        if(accelerationSensor != null){
            sensorManager.registerListener(accelerationListener, accelerationSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(getApplication<BaseApplication>(), "ไม่พบ Sensor ", Toast.LENGTH_LONG).show()
        }

    }

    fun terminateSensor(){
        sensorManager.unregisterListener(accelerationListener)
    }

    override fun onCleared() {
        super.onCleared()
        terminateSensor()

    }
}