package com.n8ify.charon.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.n8ify.charon.data.room.dao.HistoryDao
import com.n8ify.charon.data.room.dao.ResultDao
import com.n8ify.charon.model.entity.History
import com.n8ify.charon.model.entity.Result

@Database(entities = [History::class, Result::class], version = 3, exportSchema = false)
abstract class CentralDatabase : RoomDatabase() {

    abstract fun historyDao() : HistoryDao
    abstract fun resultDao() : ResultDao

    companion object {

        lateinit var centralDatabase : CentralDatabase

        fun init(context : Context){

            if(!::centralDatabase.isInitialized){
                centralDatabase = Room.databaseBuilder(context, CentralDatabase::class.java, "central.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }

        }

        fun getInstance() : CentralDatabase {
            return if(!::centralDatabase.isInitialized){
                throw IllegalStateException("Central database is never be initialized.")
            } else {
                centralDatabase
            }
        }

    }

}