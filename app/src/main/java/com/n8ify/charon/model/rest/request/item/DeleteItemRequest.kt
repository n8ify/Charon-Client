package com.n8ify.charon.model.rest.request.item

import com.n8ify.charon.model.rest.request._base.BaseRequest

class DeleteItemRequest(
        val id: Int
        , val categoryId: Int
) : BaseRequest() {

    override fun toString(): String {
        return "DeleteItemRequest(id=$id, categoryId=$categoryId)"
    }
}