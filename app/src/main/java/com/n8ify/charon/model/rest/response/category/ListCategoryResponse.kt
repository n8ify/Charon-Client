package com.n8ify.charon.model.rest.response.category

import com.n8ify.charon.model.entity.Category
import com.n8ify.charon.model.rest.response.ResponseInfo
import com.n8ify.charon.model.rest.response._base.BaseResponse

class ListCategoryResponse (responseInfo : ResponseInfo, val data : List<Category>) : BaseResponse(responseInfo)