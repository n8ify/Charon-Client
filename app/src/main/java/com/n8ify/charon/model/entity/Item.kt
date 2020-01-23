package com.n8ify.charon.model.entity

import java.sql.Timestamp

data class Item(val id : Int?, val value : String, val categoryId : Int, val attributes : Map<String, String> = HashMap(), val createDate : Timestamp? = null)