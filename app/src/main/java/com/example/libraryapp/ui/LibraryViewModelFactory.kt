package com.example.libraryapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.libraryapp.data.BookDao
import com.example.libraryapp.data.ThemeManager

class LibraryViewModelFactory(
    private val bookDao: BookDao,
    private val themeManager: ThemeManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LibraryViewModel(bookDao, themeManager) as T
    }
}