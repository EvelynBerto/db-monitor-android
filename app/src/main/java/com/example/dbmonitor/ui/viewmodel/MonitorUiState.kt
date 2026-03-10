package com.example.dbmonitor.ui.viewmodel

data class MonitorUiState(
    val currentDb: Double = 0.0,
    val isMonitoring: Boolean = false,
    val history: List<Double> = emptyList()
)
