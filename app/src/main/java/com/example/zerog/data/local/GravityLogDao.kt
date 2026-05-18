package com.example.zerog.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [GravityLog].
 *
 * All write operations are suspend functions (run on a coroutine).
 * [getAll] returns a [Flow] so the UI reacts automatically to any DB change.
 */
@Dao
interface GravityLogDao {

    // ── Create ────────────────────────────────────────────────────────────────
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: GravityLog)

    // ── Read ──────────────────────────────────────────────────────────────────
    @Query("SELECT * FROM gravity_logs ORDER BY createdAt DESC")
    fun getAll(): Flow<List<GravityLog>>

    // ── Update ────────────────────────────────────────────────────────────────
    @Update
    suspend fun update(log: GravityLog)

    // ── Delete ────────────────────────────────────────────────────────────────
    @Delete
    suspend fun delete(log: GravityLog)
}
