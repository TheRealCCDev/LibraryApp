package com.example.libraryapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.Book
import com.example.libraryapp.data.BookDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(private val bookDao: BookDao) : ViewModel() {
    private val _uiState = MutableStateFlow(LibraryUIState())
    val uiState: StateFlow<LibraryUIState> = _uiState.asStateFlow()

    fun getBookList(): Flow<List<Book>> = bookDao.getBooks()

    fun setBookRead(bookId: Int, isRead: Boolean)  {
        viewModelScope.launch { // Se usa una corrutina para que la UI no tenga que hacerlo
            bookDao.setBookRead(bookId, isRead)
        }
    }
}