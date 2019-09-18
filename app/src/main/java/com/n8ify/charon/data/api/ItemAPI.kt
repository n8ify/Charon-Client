package com.n8ify.charon.data.api

import com.n8ify.charon.model.rest.response.item.ListItemResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemAPI {

    @GET("/category/item/{id}")
    fun getCategoryItem(@Path("id") id : Int, @Query("amount") amount : Int, @Query("listPolicy") listPolicy : String) : Deferred<ListItemResponse>

}