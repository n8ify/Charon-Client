package com.n8ify.charon.presentation.item.viewmodel

import android.app.Application
import com.n8ify.charon.data.repository.ItemRepository
import com.n8ify.charon.presentation._base.viewmodel.BaseViewModel

class ItemViewModel(private val itemRepository: ItemRepository, application : Application) : BaseViewModel(application)