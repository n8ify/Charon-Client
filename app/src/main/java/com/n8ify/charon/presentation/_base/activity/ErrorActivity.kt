package com.n8ify.charon.presentation._base.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.n8ify.charon.R
import kotlinx.android.synthetic.main.activity_error.*

class ErrorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
    }

    override fun initView() {
        tv_error_message.text = intent.getStringExtra("exception")
    }
}
