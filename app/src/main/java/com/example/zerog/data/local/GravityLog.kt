package com.example.zerog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a single saved gravity calculation.
 *
 * [calculatedWeight] = [earthWeight] * (1 - [gravityReduction] / 100.0)
 * [createdAt] stores a UTC epoch millis timestamp used for ordering the log.
 */
@Entity(tableName = "gravity_logs")
data class GravityLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemName: String,
    val earthWeight: Double,
    val gravityReduction: Int,       // 0–100 %
    val calculatedWeight: Double,
    val createdAt: Long = System.currentTimeMillis()
)
