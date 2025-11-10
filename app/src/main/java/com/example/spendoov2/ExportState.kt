package com.example.spendoov2

sealed interface ExportState {
    data object Idle : ExportState
    data object Exporting : ExportState
    data object Success : ExportState
    data class Error(val message: String?) : ExportState
}