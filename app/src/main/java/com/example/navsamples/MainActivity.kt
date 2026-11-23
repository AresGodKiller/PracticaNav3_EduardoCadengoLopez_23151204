package com.example.navsamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.navsamples.ui.screens.Navigation
import com.example.navsamples.ui.theme.NavSamplesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavSamplesTheme {
                Navigation()
            }
        }
    }
}
