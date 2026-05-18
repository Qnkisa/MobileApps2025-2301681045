package com.example.zerog.data.repository

import com.example.zerog.data.local.GravityLog
import com.example.zerog.data.local.GravityLogDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository layer that abstracts the data source from the ViewModel.
 *
 * If a remote API or second data source is added in the future, only this class
 * needs to change — the ViewModel and UI stay untouched.
 */
class GravityRepository(private val dao: GravityLogDao) {

    /** Live stream of all logs, newest first. Backed by Room's Flow. */
    val allLogs: Flow<List<GravityLog>> = dao.getAll()

    suspend fun insert(log: GravityLog) = dao.insert(log)

    suspend fun update(log: GravityLog) = dao.update(log)

    suspend fun delete(log: GravityLog) = dao.delete(log)
}
