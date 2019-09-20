package com.n8ify.charon.presentation.item.activity

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import com.n8ify.charon.R
import com.n8ify.charon.constant.CommonConstant
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.presentation._base.activity.BaseActivity
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel
import com.n8ify.charon.presentation.item.fragment.GuessFragment
import com.n8ify.charon.presentation.item.fragment.PrepareFragment
import com.n8ify.charon.presentation.item.fragment.ResultDialogFragment
import com.n8ify.charon.presentation.item.misc.DetectSwipeGestureListener
import com.n8ify.charon.presentation.item.viewmodel.ItemViewModel
import kotlinx.android.synthetic.main.activity_guess.*
import kotlinx.android.synthetic.main.card_guess_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class GuessActivity : BaseActivity(), DetectSwipeGestureListener.OnDirectionChangeListener, ResultDialogFragment.ResultCallback {

    private val itemViewModel: ItemViewModel by viewModel()

    private val gestureCompat by lazy {
        GestureDetectorCompat(this@GuessActivity, DetectSwipeGestureListener.getInstance(this@GuessActivity))
    }

    private val countDownTimer by lazy {
        object : CountDownTimer(CommonConstant.DEFAULT_ROUND_TIME_MILLISECOND, 1000) {
            override fun onFinish() {
                Toast.makeText(this@GuessActivity, "Timeout!", Toast.LENGTH_LONG).show()
                showResult()
                playTimeUpTimeSound()
            }

            override fun onTick(millisUntilFinished: Long) {
                val secondLeft = millisUntilFinished / 1000
                Timber.i("Second left : %d ", secondLeft)
                if (secondLeft != 0L) {
                    tv_timer.text = secondLeft.toString()
                }
            }
        }
    }

    lateinit var guessCardFragment: GuessFragment
    lateinit var guessCard : View

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
            supportFragmentManager.beginTransaction().remove(it).commit()
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

        if (::guessCardFragment.isInitialized) {
            supportFragmentManager
                .beginTransaction()
                .remove(guessCardFragment)
                .commitAllowingStateLoss()
        }

        guessCardFragment = GuessFragment.newInstance(item)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.ll_item_container, guessCardFragment, guessCardFragment.TAG)
            .commitAllowingStateLoss()

//        if(::guessCard.isInitialized){
//            ll_item_container.removeAllViews()
//        }
//
//        guessCard = LayoutInflater.from(sthis@GuessActivity).inflate(R.layout.card_guess_item, null)
//        guessCard.tv_guess_item.text = item.value
//        ll_item_container.addView(guessCard)

    }

    private fun definedPostAction(){
        itemViewModel.guessQueue.value?.peek().let {
            if (it != null) {
                nextGuess(it)
            } else {
                showResult()
            }
        }
    }

    private fun showResult() {
        val resultDialogFragment =
            ResultDialogFragment.newInstance(itemViewModel.guessQueueResult, itemViewModel.guessQueueSize)
        supportFragmentManager
            .beginTransaction()
            .add(resultDialogFragment, resultDialogFragment.TAG)
            .commitAllowingStateLoss()
    }

    override fun onBackClick() {
        finish()
    }


    fun playTimeUpTimeSound(){
        MediaPlayer.create(this@GuessActivity, null).start()
    }
    fun playStartTimeSound(){
        MediaPlayer.create(this@GuessActivity, null).start()
    }
    fun playCorrectSound(){
        MediaPlayer.create(this@GuessActivity, null).start()
    }
    fun playSkipSound(){
        MediaPlayer.create(this@GuessActivity, null).start()
    }

}
