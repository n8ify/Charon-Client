package com.n8ify.charon.data.repository

import com.n8ify.charon.model.misc.UseCaseResult
import com.n8ify.charon.model.rest.response.category.ListCategoryResponse

interface CategoryRepository {

    suspend fun getTotalCategories() : UseCaseResult<ListCategoryResponse>

}