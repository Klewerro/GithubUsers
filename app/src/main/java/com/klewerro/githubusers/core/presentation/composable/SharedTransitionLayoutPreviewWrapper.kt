package com.klewerro.githubusers.core.presentation.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionLayoutPreviewWrapper(
    content: @Composable (Pair<SharedTransitionScope, AnimatedVisibilityScope>) -> Unit
) {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            content(this@SharedTransitionLayout to this@AnimatedVisibility)
        }
    }
}
