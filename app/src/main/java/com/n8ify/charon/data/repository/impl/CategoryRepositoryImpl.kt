package com.n8ify.charon.data.repository.impl

import com.n8ify.charon.data.api.CategoryAPI
import com.n8ify.charon.data.repository.CategoryRepository
import com.n8ify.charon.model.misc.UseCaseResult
import com.n8ify.charon.model.rest.response.category.ListCategoryResponse

class CategoryRepositoryImpl(private val categoryAPI: CategoryAPI) : CategoryRepository {

    override suspend fun getTotalCategories(): UseCaseResult<ListCategoryResponse> {

        return try {

            val response = categoryAPI.getTotalCategories().await()
            UseCaseResult.Success(response)

        } catch (e : Exception){
            UseCaseResult.Error(e)
        }

    }
}