package com.n8ify.charon.data.api

import com.n8ify.charon.model.rest.response.category.ListCategoryResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface CategoryAPI {

    @GET("/category/")
    fun getTotalCategories() : Deferred<ListCategoryResponse>

}