package com.n8ify.charon.presentation.item.viewholder

import android.graphics.Color
import android.view.View
import com.n8ify.charon.R
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.presentation._base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_result.view.*

class GuessResultViewHolder(private val view: View) : BaseViewHolder(view) {

    fun bind(result: Pair<Item, Boolean>) {
        // Note : Is a corrected guess.
        if (result.second) {
            view.tv_guess_status.setTextColor(Color.GREEN)
            view.tv_guess_status.text = getString(R.string.status_correct)
        } else {
            view.tv_guess_status.setTextColor(Color.RED)
            view.tv_guess_status.text = getString(R.string.status_skip)
        }

        view.tv_guess_item.text = result.first.value

    }

}