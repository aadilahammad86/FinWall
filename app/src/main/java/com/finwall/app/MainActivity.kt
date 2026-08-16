package com.finwall.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.finwall.app.navigation.Screen
import com.finwall.app.ui.MainViewModel
import com.finwall.app.ui.components.FloatingCircularNavBar
import com.finwall.app.ui.screens.ActivityScreen
import com.finwall.app.ui.screens.HomeScreen
import com.finwall.app.ui.screens.SettingsScreen
import com.finwall.app.ui.screens.WorkspaceScreen
import com.finwall.app.ui.theme.ExpressiveTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val uiState by viewModel.uiState.collectAsStateWithLifecycle()
      val coroutineScope = rememberCoroutineScope()

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
            // Horizontal Pager with all 4 pages pre-warmed for stutter-free swiping & jumping
            val pagerState = rememberPagerState(
              initialPage = uiState.currentScreen.index,
              pageCount = { Screen.items.size }
            )

            // Sync user swipe gesture settling -> ViewModel (only fires when swipe is finished, never during programmatic scroll)
            LaunchedEffect(pagerState) {
              snapshotFlow { pagerState.settledPage }
                .collect { settledPage ->
                  val targetScreen = Screen.items.getOrNull(settledPage) ?: Screen.Home
                  if (uiState.currentScreen != targetScreen && !pagerState.isScrollInProgress) {
                    viewModel.navigateTo(targetScreen)
                  }
                }
            }

            // In-app button programmatic navigation (e.g. from HomeScreen buttons)
            LaunchedEffect(uiState.currentScreen) {
              if (pagerState.currentPage != uiState.currentScreen.index && !pagerState.isScrollInProgress) {
                pagerState.animateScrollToPage(
                  page = uiState.currentScreen.index,
                  animationSpec = tween(
                    durationMillis = 380,
                    easing = FastOutSlowInEasing
                  )
                )
              }
            }

            HorizontalPager(
              state = pagerState,
              modifier = Modifier.fillMaxSize(),
              beyondViewportPageCount = 3
            ) { pageIndex ->
              when (Screen.items[pageIndex]) {
                Screen.Home -> HomeScreen(
                  innerPadding = innerPadding,
                  financeSummary = uiState.financeSummary,
                  onNavigateToWorkspace = { viewModel.navigateTo(Screen.Workspace) },
                  onNavigateToSettings = { viewModel.navigateTo(Screen.Settings) }
                )
                Screen.Workspace -> WorkspaceScreen(
                  innerPadding = innerPadding,
                  transactions = uiState.transactions,
                  categoriesMap = uiState.categoriesMap,
                  onAddTransaction = { viewModel.addTransaction(it) },
                  onUpdateTransaction = { viewModel.updateTransaction(it) },
                  onDeleteTransaction = { viewModel.deleteTransaction(it) },
                  onAddCustomCategory = { type, cat -> viewModel.addCustomCategory(type, cat) }
                )
                Screen.Activity -> ActivityScreen(
                  innerPadding = innerPadding,
                  activityLogs = uiState.activityLogs,
                  financeSummary = uiState.financeSummary
                )
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

            // Floating Circular Navigation Bar pinned at bottom with direct coroutine navigation
            FloatingCircularNavBar(
              currentScreen = uiState.currentScreen,
              onScreenSelected = { targetScreen ->
                if (uiState.currentScreen != targetScreen) {
                  viewModel.navigateTo(targetScreen)
                  coroutineScope.launch {
                    pagerState.animateScrollToPage(
                      page = targetScreen.index,
                      animationSpec = tween(
                        durationMillis = 380,
                        easing = FastOutSlowInEasing
                      )
                    )
                  }
                }
              },
              modifier = Modifier.align(Alignment.BottomCenter)
            )
          }
        }
      }
    }
  }
}
