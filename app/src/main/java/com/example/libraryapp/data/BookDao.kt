package com.example.libraryapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    // Retrieves all books
    @Query(
        """
            SELECT * FROM Book
        """
    )
    fun getBooks(): Flow<List<Book>>

    // Change the book status 'read'
    @Query("UPDATE Book SET read = :isRead WHERE id = :bookId")
    suspend fun setBookRead(bookId: Int, isRead: Boolean)

    // Sets the Uri to a specific book
    @Query("UPDATE Book SET file = :file WHERE id = :bookId")
    suspend fun setBookFile(bookId: Int, file: String)

    @Query(
        """
            SELECT file FROM Book
        """
    )
    fun getFiles(): Flow<List<String>>
}