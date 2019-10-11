package com.n8ify.charon.data.repository.impl

import com.n8ify.charon.data.repository.HistoryRepository
import com.n8ify.charon.data.room.CentralDatabase
import com.n8ify.charon.model.entity.History
import com.n8ify.charon.model.entity.HistoryAndResult
import com.n8ify.charon.model.entity.Item
import com.n8ify.charon.model.entity.Result
import com.n8ify.charon.model.misc.UseCaseResult
import java.util.concurrent.LinkedBlockingQueue

class HistoryRepositoryImpl : HistoryRepository {

    val database: CentralDatabase by lazy { CentralDatabase.getInstance() }

    override suspend fun insertHistoryAndResult(
        categoryName: String,
        queuedResults: LinkedBlockingQueue<Pair<Item, Boolean>>
    ): UseCaseResult<HistoryAndResult> {
        return try {

            val historyId = System.currentTimeMillis()

            val history = History(historyId, categoryName)
            database.historyDao().insert(history)

            val results = mutableListOf<Result>()
            queuedResults.forEach { queuedResult ->
                val result =
                    Result(historyId = historyId, itemName = queuedResult.first.value, itemResult = queuedResult.second)
                database.resultDao().insert(result).also {
                    results.add(result)
                }
            }

            UseCaseResult.Success(HistoryAndResult(historyId, categoryName, results))

        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }


}