package com.n8ify.charon.model.rest.response.item

import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.model.rest.response.ResponseInfo
import com.n8ify.charon.model.rest.response._base.BaseResponse

class ListItemResponse(responseInfo : ResponseInfo, val data : List<Item>) : BaseResponse(responseInfo)