package com.n8ify.charon.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "result")
data class Result(@PrimaryKey(autoGenerate = true) val id : Int = 0, val historyId : Long, val itemName : String,  val itemResult : Boolean) {
}