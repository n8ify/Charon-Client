package com.n8ify.charon.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.n8ify.charon.data.room.dao._base.BaseDao
import com.n8ify.charon.model.entity.Result

@Dao
abstract class ResultDao  {

    @Insert
    abstract fun insert(result : Result)

}