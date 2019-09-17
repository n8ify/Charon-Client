package com.n8ify.charon.model.rest.request.category

import com.n8ify.charon.model.rest.request._base.BaseRequest

class GetCategoryRequest(
        val id: Int
) : BaseRequest() {
    override fun toString(): String {
        return "ListCategoryRequest(id=$id)"
    }
}