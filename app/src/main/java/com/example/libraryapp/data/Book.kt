package com.example.libraryapp.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Book")
data class Book(
    @PrimaryKey
    val id: Int,
    @NonNull
    @ColumnInfo(name = "title")
    val title : String,
    @NonNull
    @ColumnInfo(name = "author")
    val author : String,
    @NonNull
    @ColumnInfo(name = "read")
    val isRead : Boolean
)
