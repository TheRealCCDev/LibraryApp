package com.example.libraryapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.Book
import com.example.libraryapp.data.BookDao
import com.example.libraryapp.data.ThemeManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val bookDao: BookDao,
    private val themeManager: ThemeManager
) : ViewModel() {
    val isDarkMode: StateFlow<Boolean> = themeManager.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun getBookList(): Flow<List<Book>> = bookDao.getBooks()

    fun setBookRead(bookId: Int, isRead: Boolean)  {
        viewModelScope.launch { // Se usa una corrutina para que la UI no tenga que hacerlo
            bookDao.setBookRead(bookId, isRead)
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            themeManager.toggleTheme(isDark)
        }
    }
}