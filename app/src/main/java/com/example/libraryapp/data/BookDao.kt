package com.example.libraryapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query(
        """
            SELECT * FROM Book
        """
    )
    fun getBooks(): Flow<List<Book>>

    @Query("UPDATE Book SET read = :isRead WHERE id = :bookId")
    suspend fun setBookRead(bookId: Int, isRead: Boolean)

    @Query("UPDATE Book SET file = :file WHERE id = :bookId")
    suspend fun setBookFile(bookId: Int, file: String)
}