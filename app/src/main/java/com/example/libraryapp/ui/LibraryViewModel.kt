package com.example.libraryapp.ui

import android.content.Context
import android.net.Uri
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
import java.io.File

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

    fun saveBookFile(context: Context, uri: Uri, bookId: Int) {
        viewModelScope.launch {
            try {
                // Gets the file name
                val fileName = getFileName(context, uri) ?: "libro_$bookId"

                // Sets the file
                val destinationFile = File(context.filesDir, fileName)

                // Copies the selected file to internal storage to bypass temporary URI permissions
                // and ensure the file is permanently available for local use and future S3 backups.
                context.contentResolver.openInputStream(uri)?.use { input ->
                    destinationFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                // Set file Uri to the book
                bookDao.setBookFile(bookId, destinationFile.absolutePath)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Returns the file name given the file Uri
    private fun getFileName(context: Context, uri: Uri): String? {
        var name: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex != -1) {
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }
}