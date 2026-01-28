package com.example.libraryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.libraryapp.data.ThemeManager
import com.example.libraryapp.ui.screens.LibraryScreen
import com.example.libraryapp.ui.theme.LibraryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val themeManager = ThemeManager(this)

        setContent {
            val isDark by themeManager.isDarkMode.collectAsState(initial = false)
            LibraryAppTheme(darkTheme = isDark) {
                LibraryScreen()
            }
        }
    }
}