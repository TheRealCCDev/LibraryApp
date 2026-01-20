package com.example.libraryapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LibraryScreen(
    libraryViewModel: LibraryViewModel = viewModel()
) {
    val libraryUiState by libraryViewModel.uiState.collectAsState()
}

@Preview(showBackground = true)
@Composable
fun PreviewLibraryScreen() {
    LibraryScreen()
}