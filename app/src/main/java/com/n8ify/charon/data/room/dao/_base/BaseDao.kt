package com.n8ify.charon.data.room.dao._base

import androidx.room.Insert


interface BaseDao {

    @Insert
    suspend fun <T> insert(vararg any : T)

}