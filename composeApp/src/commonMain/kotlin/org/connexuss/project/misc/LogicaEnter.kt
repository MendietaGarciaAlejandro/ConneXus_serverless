package org.connexuss.project.misc

import androidx.compose.runtime.Composable

@Composable
expect fun EnterClickable(
    onClick: () -> Unit,
    content: @Composable () -> Unit
)