package com.n8ify.charon.model.rest.request.item

import com.n8ify.charon.model.entity.Item

class CreateUpdateItemRequest(
        val id: Int? = null
        , val value: String
        , val categoryId: Int
) {

    fun toItem(): Item {

        val mustNull : Any? = null

        (mustNull as? Number)?.also { } ?:kotlin.run { }

        return Item(
                this@CreateUpdateItemRequest.id
                , this@CreateUpdateItemRequest.value
                , this@CreateUpdateItemRequest.categoryId)
    }

    override fun toString(): String {
        return "CreateUpdateItemRequest(id=$id, value='$value', categoryId=$categoryId)"
    }


}