package com.n8ify.charon.presentation.item.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import com.n8ify.charon.R
import com.n8ify.charon.constant.CommonConstant
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.presentation._base.activity.BaseActivity
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel
import com.n8ify.charon.presentation.item.fragment.GuessFragment
import com.n8ify.charon.presentation.item.fragment.PrepareFragment
import com.n8ify.charon.presentation.item.fragment.ResultDialogFragment
import com.n8ify.charon.presentation.item.misc.DetectSwipeGestureListener
import com.n8ify.charon.presentation._base.viewmodel.ItemViewModel
import kotlinx.android.synthetic.main.activity_guess.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.lang.Exception

class GuessActivity : BaseActivity(), DetectSwipeGestureListener.OnDirectionChangeListener, ResultDialogFragment.ResultCallback {

    private val itemViewModel: ItemViewModel by viewModel()

    private val gestureCompat by lazy {
        GestureDetectorCompat(this@GuessActivity, DetectSwipeGestureListener.getInstance(this@GuessActivity))
    }

    private var isCounting = true
    private val countDownTimer by lazy {
        val timeCount = try { intent.extras.getInt("timeCount") * CommonConstant.MILLISECOND } catch(e: Exception) {CommonConstant.DEFAULT_ROUND_TIME_MILLISECOND}
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

                if(secondLeft <= 5){
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

    }

    private fun prepareAndStart() {

        val prepareFragment = PrepareFragment.newInstance {
            supportFragmentManager.beginTransaction().remove(it)
                .commit()
            countDownTimer.start()
            itemViewModel.guessQueue.value?.let { guessItems ->
                nextGuess(guessItems.peek())
            }
            playStartTimeSound()
        }

        supportFragmentManager
            .beginTransaction()
            .add(prepareFragment, prepareFragment.TAG)
            .commit()

    }

    override fun initView() {
        tv_timer?.bringToFront()
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
        if(!isCounting){ return }
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
        if(!isCounting){ return }
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
        replaceFragment(GuessFragment.newInstance(item))
    }

    private fun definedPostAction(){
        // Note : Check if queue is empty, true -> immediately show result, false -> go to next guess.
        if(itemViewModel.guessQueue.value?.isEmpty() == true){
            showResult()
        }

        // Note : Peek a next item and make it as a next guess item.
        itemViewModel.guessQueue.value?.peek().let {
            if (it != null) {
                nextGuess(it)
            }
        }
    }

    private fun showResult() {
        val resultDialogFragment =
            ResultDialogFragment.newInstance(itemViewModel.guessQueueResult, itemViewModel.guessQueueSize)
        supportFragmentManager
            .beginTransaction()
            .add(resultDialogFragment, resultDialogFragment.TAG)
            .commit()
    }

    override fun onBackClick() {
        finish()
    }


    fun playTimeoutTickSound(){
        MediaPlayer.create(this@GuessActivity, R.raw.prepare_count).start()
    }

    private fun playStartTimeSound(){
        MediaPlayer.create(this@GuessActivity, R.raw.start).start()
    }

    fun playTimeUpSound(){
        MediaPlayer.create(this@GuessActivity, R.raw.timeup).start()
    }

    private fun playCorrectSound(){
        MediaPlayer.create(this@GuessActivity, R.raw.correct).start()
    }
    private fun playSkipSound(){
        MediaPlayer.create(this@GuessActivity, R.raw.skip).start()
    }

    override fun onDestroy() {
        countDownTimer.cancel()
        super.onDestroy()
    }

    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.ll_item_container, fragment)
            .commit()
    }

}
