package com.n8ify.charon.model.rest.response.category

import com.n8ify.charon.model.entity.Category
import com.n8ify.charon.model.rest.response.ResponseInfo
import com.n8ify.charon.model.rest.response._base.BaseResponse

class SingleCategoryResponse (responseInfo : ResponseInfo, val data : Category) : BaseResponse(responseInfo)