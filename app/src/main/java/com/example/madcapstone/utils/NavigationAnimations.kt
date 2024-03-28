package com.example.madcapstone.utils

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

class NavigationAnimations {
    companion object {
        private const val NAVIGATION_ANIMATION_DURATION = 300

        fun moveInFromRight(): EnterTransition {
            return slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(NAVIGATION_ANIMATION_DURATION, easing = LinearOutSlowInEasing)
            )
        }

        fun moveOutToRight(): ExitTransition {
            return slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(NAVIGATION_ANIMATION_DURATION, easing = LinearOutSlowInEasing)
            )
        }
    }
}