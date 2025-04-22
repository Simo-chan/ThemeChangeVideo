package com.example.themechangevideo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.example.themechangevideo.ui.ExampleContent
import com.example.themechangevideo.ui.theme.ThemeChangeVideoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }

            Crossfade(targetState = isDarkTheme, animationSpec = tween(1000)) { newTheme ->
                ThemeChangeVideoTheme(darkTheme = newTheme) {
                    Scaffold(
                        topBar = {
                            ExampleTopAppBar(
                                isDarkTheme = newTheme,
                                onClick = { isDarkTheme = !isDarkTheme })
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        ExampleContent(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExampleTopAppBar(
    isDarkTheme: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {},
        actions = { ThemeChangeButton(isDarkTheme = isDarkTheme, onClick = onClick) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeChangeButton(
    isDarkTheme: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var rotation by remember { mutableFloatStateOf(0f) }
    val animatable = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    IconButton(
        onClick = {
            onClick()
            coroutineScope.launch {
                animatable.animateTo(
                    targetValue = rotation + 360f,
                    animationSpec = tween(600)
                ) {
                    rotation = value
                }
            }
        }
    ) {
        Icon(
            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = null,
            modifier = Modifier.rotate(rotation)
        )
    }
}

