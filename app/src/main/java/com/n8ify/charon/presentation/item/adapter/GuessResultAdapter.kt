package com.n8ify.charon.presentation.item.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n8ify.charon.R
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.presentation.item.viewholder.GuessResultViewHolder
import java.util.concurrent.LinkedBlockingQueue

class GuessResultAdapter : RecyclerView.Adapter<GuessResultViewHolder>() {

    companion object {

        fun newInstance(guessQueueResult: LinkedBlockingQueue<Pair<Item, Boolean>>): GuessResultAdapter {
            return GuessResultAdapter().apply {
                this@apply.guessQueueResult = guessQueueResult.toList()
            }
        }

    }

    private lateinit var guessQueueResult: List<Pair<Item, Boolean>>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuessResultViewHolder {
        return GuessResultViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false))
    }

    override fun getItemCount(): Int = this@GuessResultAdapter.guessQueueResult.size


    override fun onBindViewHolder(holder: GuessResultViewHolder, position: Int) {
        holder.bind(this@GuessResultAdapter.guessQueueResult[position])
    }
}