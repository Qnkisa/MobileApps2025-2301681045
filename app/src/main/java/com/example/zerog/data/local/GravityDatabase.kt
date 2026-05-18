package com.example.zerog.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database singleton.
 *
 * Increment [version] and provide a migration strategy whenever the schema changes.
 * [exportSchema] is false here to keep the project clean; set to true and configure
 * a schema export directory for production/test coverage if needed.
 */
@Database(
    entities = [GravityLog::class],
    version = 1,
    exportSchema = false
)
abstract class GravityDatabase : RoomDatabase() {

    abstract fun gravityLogDao(): GravityLogDao

    companion object {
        @Volatile
        private var INSTANCE: GravityDatabase? = null

        fun getInstance(context: Context): GravityDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    GravityDatabase::class.java,
                    "gravity_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
