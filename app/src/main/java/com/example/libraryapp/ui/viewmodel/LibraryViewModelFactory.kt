package com.example.libraryapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.libraryapp.data.BookDao
import com.example.libraryapp.data.ThemeManager

class LibraryViewModelFactory(
    private val application: Application,
    private val bookDao: BookDao,
    private val themeManager: ThemeManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibraryViewModel(application, bookDao, themeManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }
}