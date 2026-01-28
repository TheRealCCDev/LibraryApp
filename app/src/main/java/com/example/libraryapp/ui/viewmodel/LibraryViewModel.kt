package com.example.libraryapp.ui.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.libraryapp.data.AppDatabase
import com.example.libraryapp.data.Book
import com.example.libraryapp.data.BookDao
import com.example.libraryapp.data.ThemeManager
import com.example.libraryapp.data.workers.UploadBooksWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.TimeUnit

class LibraryViewModel(
    application: Application,
    private val bookDao: BookDao,
    private val themeManager: ThemeManager
) : AndroidViewModel(application) {

    init {
        scheduleDailyUpload()

//        val testRequest = OneTimeWorkRequestBuilder<UploadBooksWorker>().build()
//        WorkManager.getInstance(getApplication()).enqueue(testRequest)

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(getApplication())
            val bookDao = db.bookDao()

            // Obtenemos la lista real de archivos
            val fileList: List<String?> = bookDao.getFiles().first() // puede haber nulls
            fileList.forEach { path ->
                if (!path.isNullOrEmpty()) {
                    val file = File(path)
                    Log.i("FileCheck", "Archivo: $path, existe: ${file.exists()}")
                } else {
                    Log.w("FileCheck", "Se encontró un path nulo en la base de datos")
                }
            }

        }
    }

    fun getBookList(): Flow<List<Book>> = bookDao.getBooks()

    fun getFileList(): Flow<List<String>> = bookDao.getFiles()

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

                /* --- OPCIÓN 2: DocumentFile (Storage Access Framework) ---
                Uso: Para trabajar con árboles de directorios o archivos en SD/Nube.

                val pickedDir = DocumentFile.fromTreeUri(context, someDirectoryUri)
                val newFile = pickedDir?.createFile("application/pdf", fileName)
                newFile?.uri?.let { targetUri ->
                    context.contentResolver.openOutputStream(targetUri)?.use { output ->
                        context.contentResolver.openInputStream(uri)?.copyTo(output)
                    }
                }
                */

                /* --- OPCIÓN 3: MediaStore (Archivos Públicos) ---
                Uso: Para guardar el libro en la carpeta pública de 'Downloads' o 'Documents'.

                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
                }
                val externalUri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)
                externalUri?.let { target ->
                    context.contentResolver.openOutputStream(target)?.use { output ->
                        context.contentResolver.openInputStream(uri)?.copyTo(output)
                    }
                }
                */

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
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex != -1) {
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }

    private fun scheduleDailyUpload() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        // Crea la request para que se ejecute cada 24 horas
        val dailyRequest = PeriodicWorkRequestBuilder<UploadBooksWorker>(24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        // Usamos getApplication() para evitar leaks
        WorkManager.getInstance(getApplication()).enqueueUniquePeriodicWork(
            "upload_books_task",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyRequest
        )
    }
}