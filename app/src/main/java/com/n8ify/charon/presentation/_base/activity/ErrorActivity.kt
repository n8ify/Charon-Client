package com.n8ify.charon.presentation._base.activity

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.view.View
import com.n8ify.charon.R
import kotlinx.android.synthetic.main.activity_error.*

class ErrorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        tv_error_title.setOnLongClickListener {
            val itemClipData = ClipData.Item("Various Clip Data")
            val mimeType = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)

            val clipData = ClipData(it.tag.toString(), mimeType, itemClipData)

            val shadow = View.DragShadowBuilder(tv_error_title)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(clipData, shadow, null, 0)
            } else {
                it.startDrag(clipData, shadow, null, 0)
            }

            true
        }
        tv_error_title.setOnDragListener { v, event ->
            true
        }

    }

}
