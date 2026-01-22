package com.example.libraryapp.ui

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.libraryapp.data.AppDatabase
import com.example.libraryapp.data.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    context: Context
) {
    val db = AppDatabase.getDatabase(context)
    val factory = LibraryViewModelFactory(db.bookDao())

    val libraryViewModel: LibraryViewModel =
        viewModel(factory = factory)

    val bookList by libraryViewModel
        .getBookList()
        .collectAsState(emptyList())

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
                ) {
                    Libro(book)
                }
            }
        }
    }
}

@Composable
fun Libro(book: Book) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
    ) {
        Text(
            text = book.title,
            modifier = Modifier
                .padding(8.dp),
            textAlign = TextAlign.Center,
        )
        Text(
            text = book.author,
            modifier = Modifier
                .padding(8.dp),
            textAlign = TextAlign.Center,
        )
    }

}

//@Preview(showBackground = true)
//@Composable
//fun PreviewLibraryScreen() {
//    LibraryScreen()
//}