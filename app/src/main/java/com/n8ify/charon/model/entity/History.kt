package com.n8ify.charon.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(@PrimaryKey val id : Long, val categoryName : String)