package com.example.libraryapp.ui

import androidx.lifecycle.ViewModel
import com.example.libraryapp.data.Book
import com.example.libraryapp.data.BookDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LibraryViewModel(private val bookDao: BookDao) : ViewModel() {
    private val _uiState = MutableStateFlow(LibraryUIState())
    val uiState: StateFlow<LibraryUIState> = _uiState.asStateFlow()

    fun getBookList(): Flow<List<Book>> = bookDao.getBooks()
}