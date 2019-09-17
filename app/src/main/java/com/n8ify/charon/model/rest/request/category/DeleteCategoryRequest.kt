package com.n8ify.charon.model.rest.request.category

import com.n8ify.charon.model.rest.request._base.BaseRequest

class DeleteCategoryRequest(
        val id: Int
) : BaseRequest() {

    override fun toString(): String {
        return "CreateCategoryRequest(id=$id)"
    }

}