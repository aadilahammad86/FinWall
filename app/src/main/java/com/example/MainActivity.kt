package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.navigation.Screen
import com.example.ui.MainViewModel
import com.example.ui.components.FloatingCircularNavBar
import com.example.ui.screens.ActivityScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WorkspaceScreen
import com.example.ui.theme.ExpressiveTheme

class MainActivity : ComponentActivity() {
  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val uiState by viewModel.uiState.collectAsStateWithLifecycle()

      ExpressiveTheme(
        themeMode = uiState.themeMode,
        dynamicColor = uiState.isDynamicMonetEnabled,
        seedColor = uiState.selectedSeedColor
      ) {
        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(MaterialTheme.colorScheme.background)
          ) {
            // Page Transition Container
            AnimatedContent(
              targetState = uiState.currentScreen,
              transitionSpec = {
                val isForward = targetState.index >= initialState.index
                val springSpec = spring<Float>(
                  dampingRatio = if (uiState.isBouncyMotion) Spring.DampingRatioMediumBouncy else Spring.DampingRatioNoBouncy,
                  stiffness = Spring.StiffnessMediumLow
                )

                if (isForward) {
                  (slideInHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) { width -> width / 3 } +
                      fadeIn() + scaleIn(initialScale = 0.95f)) togetherWith
                      (slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) { width -> -width / 3 } +
                          fadeOut() + scaleOut(targetScale = 0.95f))
                } else {
                  (slideInHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) { width -> -width / 3 } +
                      fadeIn() + scaleIn(initialScale = 0.95f)) togetherWith
                      (slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) { width -> width / 3 } +
                          fadeOut() + scaleOut(targetScale = 0.95f))
                }.using(SizeTransform(clip = false))
              },
              label = "page_transition"
            ) { targetScreen ->
              when (targetScreen) {
                Screen.Home -> HomeScreen(
                  innerPadding = innerPadding,
                  onNavigateToSettings = { viewModel.navigateTo(Screen.Settings) }
                )
                Screen.Workspace -> WorkspaceScreen(innerPadding = innerPadding)
                Screen.Activity -> ActivityScreen(innerPadding = innerPadding)
                Screen.Settings -> SettingsScreen(
                  innerPadding = innerPadding,
                  uiState = uiState,
                  onSetThemeMode = { viewModel.setThemeMode(it) },
                  onSetDynamicMonetEnabled = { viewModel.setDynamicMonetEnabled(it) },
                  onSetSeedColor = { viewModel.setSeedColor(it) },
                  onToggleBouncyMotion = { viewModel.toggleBouncyMotion() }
                )
              }
            }

            // Floating Circular Navigation Bar pinned at bottom
            FloatingCircularNavBar(
              currentScreen = uiState.currentScreen,
              onScreenSelected = { screen -> viewModel.navigateTo(screen) },
              modifier = Modifier.align(Alignment.BottomCenter)
            )
          }
        }
      }
    }
  }
}

