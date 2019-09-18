package com.n8ify.charon.data.repository.impl

import com.n8ify.charon.data.api.ItemAPI
import com.n8ify.charon.data.repository.ItemRepository
import com.n8ify.charon.model.misc.UseCaseResult
import com.n8ify.charon.model.rest.response.item.ListItemResponse
import java.lang.Exception

class ItemRepositoryImpl(private val itemAPI: ItemAPI) : ItemRepository {

    override suspend fun getItem(id: Int, amount : Int, listPolicy : String): UseCaseResult<ListItemResponse> {
        val result = itemAPI.getCategoryItem(id = id, amount = amount, listPolicy = listPolicy).await()
        return try {
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

}