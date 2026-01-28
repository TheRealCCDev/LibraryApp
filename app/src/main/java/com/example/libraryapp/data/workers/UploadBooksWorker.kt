package com.example.libraryapp.data.workers

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectResponse
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.fromFile
import com.example.libraryapp.data.AppDatabase
import com.example.libraryapp.data.BookDao
import com.example.libraryapp.ui.viewmodel.LibraryViewModel
import java.io.File

class UploadBooksWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        Log.i(TAG, "Worker iniciado") // <- agrega esto para confirmar
        try {
            // Obtén la instancia de tu DB
            val db = AppDatabase.getDatabase(applicationContext)
            val bookDao = db.bookDao()

            uploadPngFilesToS3(bookDao, "library-app-backus", "fotos")
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }    }

    private suspend fun uploadPngFilesToS3(bookDao: BookDao, bucketName: String, folder: String) {
        val s3 = S3Client {
            region = "us-east-1"
            credentialsProvider = aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider {
                accessKeyId = ""
                secretAccessKey = ""
                sessionToken = ""
            }
        } // Ajusta tu región

        // Obtenemos los archivos desde Room
        bookDao.getFiles().collect { fileList ->
            fileList.forEach { path ->
                if (path.isNullOrBlank()) {
                    Log.w(TAG, "Se encontró un path nulo o vacío en la base de datos")
                    return@forEach
                }
                val file = File(path)
                if (file.exists() && (file.extension.lowercase() == "png" || file.extension.lowercase() == "jpg")) {
                    // Subir archivo
                    val request = PutObjectRequest {
                        bucket = bucketName
                        key = "$folder/${file.name}"
                        body = ByteStream.fromFile(file)
                    }
                    try {
                        val response: PutObjectResponse = s3.putObject(request)
                        Log.i(TAG, "Archivo subido: ${file.name}, ETag: ${response.eTag}")
                    } catch (e: Exception) {
                        Log.e(TAG, "Error subiendo ${file.name}: ${e.message}")
                    }
                } else {
                    Log.w(TAG, "Archivo no existe o no es PNG/JPG: $path")
                }
            }

        }

        s3.close()
    }

}