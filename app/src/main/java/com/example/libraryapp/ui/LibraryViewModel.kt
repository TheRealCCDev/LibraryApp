package com.example.libraryapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LibraryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LibraryUIState())
    val uiState: StateFlow<LibraryUIState> = _uiState.asStateFlow()
}