package com.example.libraryapp.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    libraryViewModel: LibraryViewModel = viewModel()
) {
    val libraryUiState by libraryViewModel.uiState.collectAsState()
    val bookList by libraryViewModel.getBookList().collectAsState(emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Library App") }
            )
        }
    ) { innnerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innnerPadding)
                .fillMaxSize()
        ) {
            items(bookList) { book ->
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .border(1.dp, Color.Black)
                ) {
                    Text(
                        text = book.title,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = book.author,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLibraryScreen() {
    LibraryScreen()
}