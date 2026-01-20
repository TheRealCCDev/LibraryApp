package com.example.libraryapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query(
        """
            SELECT * FROM book
        """
    )
    fun getBooks(): Flow<List<Book>>
}