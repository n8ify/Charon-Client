package com.n8ify.charon.model.entity

import java.sql.Timestamp

data class Category(val id : Int? = null, val name : String, val createDate : Timestamp? = null)