package com.example.zerog.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zerog.data.local.GravityDatabase
import com.example.zerog.data.local.GravityLog
import com.example.zerog.data.repository.GravityRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel shared between [CalculatorFragment] and [HistoryFragment].
 *
 * Extends [AndroidViewModel] to safely access [Application] context for the
 * database singleton — avoiding context leaks that would occur with Activity context.
 *
 * [allLogs] is a [StateFlow] so the UI collects it with `repeatOnLifecycle`,
 * and the 5-second timeout stops upstream collection when the app backgrounds,
 * preserving the last emitted list so the UI can restore instantly on resume.
 */
class GravityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GravityRepository

    val allLogs: StateFlow<List<GravityLog>>

    init {
        val dao = GravityDatabase.getInstance(application).gravityLogDao()
        repository = GravityRepository(dao)
        allLogs = repository.allLogs
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    }

    // ── Write operations — fire-and-forget on the IO dispatcher (Room handles this) ──

    fun insert(log: GravityLog) {
        viewModelScope.launch { repository.insert(log) }
    }

    fun update(log: GravityLog) {
        viewModelScope.launch { repository.update(log) }
    }

    fun delete(log: GravityLog) {
        viewModelScope.launch { repository.delete(log) }
    }
}
