package com.n8ify.charon.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.n8ify.charon.model.entity.History
import com.n8ify.charon.model.entity.HistoryAndResult

@Dao
abstract class HistoryDao {

    @Insert
    abstract fun insert(result : History)

    @Query("SELECT id, categoryName FROM history;")
    abstract suspend fun getHistoryAndResult() : List<HistoryAndResult>

}