package com.n8ify.charon.presentation.item.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.n8ify.charon.R
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.presentation._base.fragment.BaseFragment
import kotlinx.android.synthetic.main.card_guess_item.view.*

@Deprecated("Use GuessedItemLayout instead")
class GuessFragment : BaseFragment() {

    val TAG = GuessFragment::class.java.simpleName

    companion object {

        fun newInstance(item : Item) : GuessFragment {
            return GuessFragment().apply {
                this@apply.item = item
            }
        }

    }

    private lateinit var item: Item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.card_guess_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view?.let {
            it.tv_guess_item.text = item.value
        }
    }

}