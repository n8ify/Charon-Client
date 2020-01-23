package com.n8ify.charon.presentation.item.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.n8ify.charon.R
import com.n8ify.charon.constant.CommonConstant
import com.n8ify.charon.constant.ItemAttributeConstant
import com.n8ify.charon.constant.RemoteConfigConstant
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.presentation._base.activity.BaseActivity
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel
import com.n8ify.charon.presentation._base.viewmodel.ItemViewModel
import com.n8ify.charon.presentation._base.viewmodel.SensorViewModel
import com.n8ify.charon.presentation.item.fragment.GuessFragment
import com.n8ify.charon.presentation.item.fragment.PrepareFragment
import com.n8ify.charon.presentation.item.fragment.ResultDialogFragment
import com.n8ify.charon.presentation.item.misc.DetectSwipeGestureListener
import kotlinx.android.synthetic.main.activity_guess.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class GuessActivity : BaseActivity(), DetectSwipeGestureListener.OnDirectionChangeListener,
    ResultDialogFragment.ResultCallback {

    private val itemViewModel: ItemViewModel by viewModel()
    private val sensorViewModel: SensorViewModel by viewModel()

    private val gestureCompat by lazy {
        GestureDetectorCompat(
            this@GuessActivity,
            DetectSwipeGestureListener.getInstance(this@GuessActivity)
        )
    }

    private var isCounting = true
    private val countDownTimer by lazy {
        val timeCount = try {
            intent.extras.getInt("timeCount") * CommonConstant.MILLISECOND
        } catch (e: Exception) {
            CommonConstant.DEFAULT_ROUND_TIME_MILLISECOND
        }
        object : CountDownTimer(timeCount, 1000) {
            override fun onFinish() {
                Toast.makeText(this@GuessActivity, "Timeout!", Toast.LENGTH_LONG).show()
                showResult()
                playTimeUpSound()
                isCounting = false
            }

            override fun onTick(millisUntilFinished: Long) {

                val secondLeft = millisUntilFinished / 1000

                Timber.i("Second left : %d ", secondLeft)
                tv_timer.text = secondLeft.toString()

                if (secondLeft <= 5) {
                    playTimeoutTickSound()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)
        itemViewModel.guessQueue.observe(this, androidx.lifecycle.Observer {
            Timber.i("Remaining : %s", it.size)
            Timber.i("Left(s) : %s", it)
        }).also { itemViewModel.getItem(intent.extras.getInt("categoryId")) }
        sensorViewModel.accZ.observe(this@GuessActivity, Observer {
            Timber.d("Z : $it")
        }).also {
            sensorViewModel.initialSensor()
        }
    }

    private fun prepareAndStart() {

        val prepareFragment = PrepareFragment.newInstance {
            supportFragmentManager.beginTransaction().remove(it)
                .commit()
            countDownTimer.start()
            itemViewModel.guessQueue.value?.let { guessItems ->
                nextGuess(guessItems.peek())
            }

//            itemViewModel.initialSensor().also {
//                itemViewModel.accY.observe(this@GuessActivity, Observer {deltaY ->
//
//                    if(deltaY >= 0.6F){
//                        onUp()
//                    }
//                    if(deltaY <= 0.3){
//                        onDown()
//                    }
//
//                })
//            }

            playStartTimeSound()
        }

        supportFragmentManager
            .beginTransaction()
            .add(prepareFragment, prepareFragment.TAG)
            .commit()

    }

    override fun initView() {
    }

    override fun initObserver(vararg baseViewModels: BaseViewModel) {

        super.initObserver(itemViewModel)
        itemViewModel.isOnProgress.observe(this, androidx.lifecycle.Observer {
            if (!it) {
                prepareAndStart()
            }
        })


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event).also {
            gestureCompat.onTouchEvent(event)
        }
    }

    override fun onUp() {
        if (!isCounting) {
            return
        }
        if (itemViewModel.guessQueue.value?.isEmpty() == true) {
            showResult(); return
        }

        itemViewModel.guessQueue.value?.remove()?.let {
            println("Correct! ${itemViewModel.guessQueue.value}")
            itemViewModel.correct(it)
            playCorrectSound()
        }

        itemViewModel.guessQueue.value?.peek()?.let {
            definedPostAction()
        }
    }

    override fun onDown() {
        if (!isCounting) {
            return
        }
        if (itemViewModel.guessQueue.value?.isEmpty() == true) {
            showResult(); return
        }

        itemViewModel.guessQueue.value?.remove()?.let {
            println("Skip! ${itemViewModel.guessQueue.value}")
            itemViewModel.skip(it)
            playSkipSound()
        }

        itemViewModel.guessQueue.value?.peek()?.let {
            definedPostAction()
        }
    }

    override fun onRight() {
        println("Right!")
    }


    override fun onLeft() {
        println("Left!")
    }

    private fun nextGuess(item: Item) {
        replaceFragment(GuessFragment.newInstance(item)).also {
            if (true) {
                runAttribute()
            } else {

            }
        }
    }

    private fun definedPostAction() {
        // Note : Check if queue is empty, true -> immediately show result, false -> go to next guess.
        if (itemViewModel.guessQueue.value?.isEmpty() == true) {
            showResult()
        }

        // Note : Peek a next item and make it as a next guess item.
        itemViewModel.guessQueue.value?.peek().let {
            if (it != null) {
                nextGuess(it)
            }
        }
    }

    var attributeHolder : Pair<String, String>? = null

    private fun runAttribute() {


        val HODT_WEIGHT = ItemAttributeConstant.HURRY_OR_DEDUCT_TIME to pref.getLong(
            RemoteConfigConstant.IA_HURRY_OR_DEDUCT_WEIGHT,
            0
        )
        val HAIT_WEIGHT = ItemAttributeConstant.HURRY_AND_INCREASE_TIME to pref.getLong(
            RemoteConfigConstant.IA_HURRY_AND_INCREASE_WEIGHT,
            0
        )
        val HOE_WEIGHT = ItemAttributeConstant.HURRY_OR_END to pref.getLong(
            RemoteConfigConstant.IA_HURRY_OR_END_WEIGHT,
            0
        )
        /** TODO : map more attribute weight */


        listOf(HODT_WEIGHT, HAIT_WEIGHT, HOE_WEIGHT).sortedByDescending { it.second }.run {

            val shouldAttributeProceed = fun(): Boolean {
                return (Math.random() * (100 - 1 + 1) + 1) <= pref.getLong(
                    RemoteConfigConstant.IA_RANDOM_RATE,
                    -1
                )
            }

            while (iterator().hasNext()) {
                val attributeAndWeight = iterator().next()
                when (attributeAndWeight.first) {
                    ItemAttributeConstant.HURRY_OR_DEDUCT_TIME -> {
                        if(shouldAttributeProceed.invoke()){
                            val HODT_MAX = pref.getLong(RemoteConfigConstant.IA_HURRY_OR_DEDUCT_TIME_MIN, -1)
                            val HODT_MIN = pref.getLong(RemoteConfigConstant.IA_HURRY_OR_DEDUCT_TIME_MAX, -1)
                            var hodtGeneratedValue = (Math.random() * (HODT_MAX - HODT_MIN + 1) + HODT_MIN)


                            return@run
                        } else {
                            Timber.d("${attributeAndWeight.first} not proceed on this round")
                        }
                    }
                    ItemAttributeConstant.HURRY_AND_INCREASE_TIME -> {
                        if(shouldAttributeProceed.invoke()){
                            val HAIT_MAX = pref.getLong(RemoteConfigConstant.IA_HURRY_AND_INCREASE_TIME_MAX, -1)
                            val HAIT_MIN = pref.getLong(RemoteConfigConstant.IA_HURRY_AND_INCREASE_TIME_MIN, -1)
                            var haitGeneratedValue = (Math.random() * (HAIT_MAX - HAIT_MIN + 1) + HAIT_MIN)

                            GlobalScope.launch {
                                delay(1000).also {

                                }

                            }

                            return@run
                        } else {
                            Timber.d("${attributeAndWeight.first} not proceed on this round")
                        }
                    }
                    ItemAttributeConstant.HURRY_OR_END -> {
                        if(shouldAttributeProceed.invoke()){
                            val HOE_MAX = pref.getLong(RemoteConfigConstant.IA_HURRY_OR_END_MIN, -1)
                            val HOE_MIN = pref.getLong(RemoteConfigConstant.IA_HURRY_OR_END_MAX, -1)
                            val hoeGeneratedValue = (Math.random() * (HOE_MAX - HOE_MIN + 1) + HOE_MIN)
                            return@run
                        } else {
                            Timber.d("${attributeAndWeight.first} not proceed on this round")
                        }
                    }
                    /** TODO : add more attribute case */
                    else -> {
                        Timber.d(
                            GuessActivity::class.java.simpleName,
                            "Skip to attach an attribute on this round"
                        )
                    }
                }
            }
        }

    }

    private fun showResult() {
        countDownTimer.cancel()
        val resultDialogFragment =
            ResultDialogFragment.newInstance(
                itemViewModel.guessQueueResult,
                itemViewModel.guessQueueSize
            )

        // Save result to local database.
        intent.extras.getString("categoryName")?.let {
            itemViewModel.endgame(it)
        }

        supportFragmentManager
            .beginTransaction()
            .add(resultDialogFragment, resultDialogFragment.TAG)
            .commit()
    }

    override fun onBackClick() {
        finish()
    }


    fun playTimeoutTickSound() {
        MediaPlayer.create(this@GuessActivity, R.raw.prepare_count).start()
    }

    private fun playStartTimeSound() {
        MediaPlayer.create(this@GuessActivity, R.raw.start).start()
    }

    fun playTimeUpSound() {
        MediaPlayer.create(this@GuessActivity, R.raw.timeup).start()
    }

    private fun playCorrectSound() {
        MediaPlayer.create(this@GuessActivity, R.raw.correct).start()
    }

    private fun playSkipSound() {
        MediaPlayer.create(this@GuessActivity, R.raw.skip).start()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        countDownTimer.cancel()
        super.onDestroy()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.ll_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}

