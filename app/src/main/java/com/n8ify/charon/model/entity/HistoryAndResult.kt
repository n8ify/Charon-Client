package com.n8ify.charon.model.entity

import androidx.room.Relation

data class HistoryAndResult(val id : Long, val categoryName : String, @Relation(entity = Result::class, parentColumn = "id", entityColumn = "historyId") val results : List<Result> = arrayListOf())