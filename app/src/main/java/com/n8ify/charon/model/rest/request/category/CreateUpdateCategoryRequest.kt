package com.n8ify.charon.model.rest.request.category

import com.n8ify.charon.model.entity.Category
import com.n8ify.charon.model.rest.request._base.BaseRequest

class CreateUpdateCategoryRequest(
        val id: Int? = null
        , val name: String
) : BaseRequest() {

    fun toCategory() : Category {
        return Category(
                this@CreateUpdateCategoryRequest.id
                , this@CreateUpdateCategoryRequest.name)
    }

    override fun toString(): String {
        return "CreateCategoryRequest(id=$id, name='$name')"
    }

}