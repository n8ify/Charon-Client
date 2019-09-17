package com.n8ify.charon.model.rest.request.item

import com.n8ify.charon.model.rest.request._base.BaseRequest

class ListItemRequest(
        val id: Int?
        , val amount: Int = 1
        , val listPolicy: String = LIST_POLICY_SEQUENCE_ASC
) : BaseRequest() {

    companion object {

        /** Randomize listing result */
        const val LIST_POLICY_RANDOM = "random"

        /** Get ascended result sequentially. */
        const val LIST_POLICY_SEQUENCE_ASC = "asc_sequence"

        /** Get descended result sequentially. */
        const val LIST_POLICY_SEQUENCE_DESC = "desc_sequence"

    }

    override fun toString(): String {
        return "ListItemRequest(id=$id)"
    }

}