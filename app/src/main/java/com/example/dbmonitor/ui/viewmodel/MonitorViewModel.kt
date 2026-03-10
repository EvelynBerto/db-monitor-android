package com.example.dbmonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbmonitor.data.repository.DecibelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MonitorViewModel(
    private val repository: DecibelRepository,
    private val dispatcher: CoroutineDispatcher = kotlinx.coroutines.Dispatchers.Main.immediate
) : ViewModel() {

    private var monitoringJob: Job? = null

    private val _uiState = MutableStateFlow(MonitorUiState())
    val uiState = _uiState.asStateFlow()

    fun toggleMonitoring() {
        if (_uiState.value.isMonitoring) {
            stopMonitoring()
        } else {
            startMonitoring()
        }
    }

    private fun startMonitoring() {
        _uiState.update { it.copy(isMonitoring = true) }

        monitoringJob = viewModelScope.launch(dispatcher) {
            repository.getDecibelStream()
                .collect { db ->
                    _uiState.update { state ->
                        val newHistory = (state.history + db).takeLast(50)
                        state.copy(
                            currentDb = db,
                            history = newHistory
                        )
                    }
                }
        }
    }

    private fun stopMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = null

        _uiState.update { it.copy(isMonitoring = false, currentDb = 0.0) }
    }

    override fun onCleared() {
        super.onCleared()
        monitoringJob?.cancel()
    }
}
