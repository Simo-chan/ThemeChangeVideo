package com.example.themechangevideo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.themechangevideo.ui.ExampleContent
import com.example.themechangevideo.ui.theme.ThemeChangeVideoTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ExampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.themeState.value.isLoading
            }
        }
        setContent {
            val koinViewModel = koinViewModel<ExampleViewModel>()
            val themeState by koinViewModel.themeState.collectAsStateWithLifecycle()

            if (!themeState.isLoading) {
                MainContent(
                    isDarkTheme = themeState.isDarkTheme,
                    onClick = { viewModel.setNewTheme() }
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    isDarkTheme: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = isDarkTheme,
        animationSpec = tween(1000)
    ) { newTheme ->
        ThemeChangeVideoTheme(darkTheme = newTheme) {
            Scaffold(
                topBar = {
                    ExampleTopAppBar(
                        isDarkTheme = newTheme,
                        onClick = onClick
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                ExampleContent(modifier = Modifier.padding(innerPadding))
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

