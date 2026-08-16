package com.finwall.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finwall.app.navigation.Screen

@Composable
fun FloatingCircularNavBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Floating pill surface - directly wraps children on each frame for 100% parallel motion
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            tonalElevation = 6.dp,
            shadowElevation = 8.dp,
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
            ),
            modifier = Modifier.shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.15f)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Screen.items.forEach { screen ->
                    val isSelected = currentScreen.route == screen.route

                    FloatingCircularNavItem(
                        screen = screen,
                        isSelected = isSelected,
                        onClick = { onScreenSelected(screen) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingCircularNavItem(
    screen: Screen,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "nav_scale_${screen.route}"
    )

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "nav_bg_${screen.route}"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "nav_icon_${screen.route}"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .testTag("nav_item_${screen.route}")
            .height(48.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(containerColor, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                    radius = 28.dp
                ),
                onClick = onClick
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
            contentDescription = screen.title,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )

        // Single unified width driver: AnimatedVisibility expand/shrink
        AnimatedVisibility(
            visible = isSelected,
            enter = expandHorizontally(
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                expandFrom = Alignment.Start
            ) + fadeIn(
                animationSpec = tween(durationMillis = 200, delayMillis = 50, easing = FastOutSlowInEasing)
            ),
            exit = shrinkHorizontally(
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                shrinkTowards = Alignment.Start
            ) + fadeOut(
                animationSpec = tween(durationMillis = 120, easing = FastOutSlowInEasing)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = screen.title,
                    color = iconColor,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}
