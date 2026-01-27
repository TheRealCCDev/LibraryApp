package com.example.libraryapp.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.libraryapp.data.AppDatabase
import com.example.libraryapp.data.Book
import com.example.libraryapp.data.ThemeManager
import kotlinx.coroutines.launch
import com.example.libraryapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    context: Context
) {
    val themeManager = remember { ThemeManager(context) }
    val db = AppDatabase.getDatabase(context)
    val factory = LibraryViewModelFactory(db.bookDao(), themeManager)
    val isDark by themeManager.isDarkMode.collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    val libraryViewModel: LibraryViewModel =
        viewModel(factory = factory)

    val bookList by libraryViewModel
        .getBookList()
        .collectAsState(emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Library App") },
                actions = {
                    IconButton(onClick = {
                        scope.launch { themeManager.toggleTheme(!isDark) }
                    }) {
                        Icon(
                            painter = painterResource(
                                id = if (isDark) R.drawable.light_mode_24px else R.drawable.dark_mode_24px
                            ),
                            contentDescription = "Cambiar Tema"
                        )
                    }
                }
            )
        }
    ) { innnerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innnerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            items(bookList) { book ->
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                ) {
                    Libro(book, libraryViewModel)
                }
            }
        }
    }
}

@Composable
fun Libro(
    book: Book,
    libraryViewModel: LibraryViewModel) {

    val context = LocalContext.current

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 340.dp, height = 150.dp)
    ) {
        Text(
            text = book.title,
            modifier = Modifier
                .padding(8.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(
            text = book.author,
            modifier = Modifier
                .padding(8.dp),
            textAlign = TextAlign.Center,
        )

        IconButton(
            onClick = {
                libraryViewModel.setBookRead(book.id, !book.isRead)
            },
            modifier = Modifier
                .padding(4.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(40.dp),
                imageVector = if (book.isRead) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                contentDescription = "Marcar como leído",
                tint = if (book.isRead) Color(0xFF4CAF50) else Color.Gray
            )
        }

        DocumentPickerScreen(context, book.id, libraryViewModel)
    }
}

@Composable
fun DocumentPickerScreen(
    context: Context,
    bookId: Int,
    viewModel: LibraryViewModel
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.saveBookFile(context, it, bookId)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { launcher.launch("*/*")}) {
            Text("Seleccionar libro")
        }
    }
}