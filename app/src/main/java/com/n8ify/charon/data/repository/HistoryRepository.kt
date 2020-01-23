package com.n8ify.charon.data.repository

import com.n8ify.charon.model.entity.HistoryAndResult
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.model.misc.UseCaseResult
import java.util.concurrent.LinkedBlockingQueue

interface HistoryRepository {

    suspend fun insertHistoryAndResult(categoryName : String, queuedResult : LinkedBlockingQueue<Pair<Item, Boolean>>) : UseCaseResult<HistoryAndResult>

}