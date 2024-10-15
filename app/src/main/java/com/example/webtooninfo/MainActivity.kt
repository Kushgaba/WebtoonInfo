package com.example.webtooninfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import com.example.webtooninfo.ui.WebtoonApp
import com.example.webtooninfo.ui.theme.WebtoonInfoTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebtoonInfoTheme {
                val systemUiController = rememberSystemUiController()
                val statusBarColor = Color(46, 171, 43, 255)

                systemUiController.setStatusBarColor(
                    color = statusBarColor,
                    darkIcons = false
                )
                systemUiController.setNavigationBarColor(
                    color = Color.Black
                )
                WebtoonApp()
            }
        }
    }
}
