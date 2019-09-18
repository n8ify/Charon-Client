package com.n8ify.charon.data.repository

import com.n8ify.charon.model.misc.UseCaseResult
import com.n8ify.charon.model.rest.response.item.ListItemResponse

interface ItemRepository {

    suspend fun getItem(id : Int, amount : Int, listPolicy : String) : UseCaseResult<ListItemResponse>

}