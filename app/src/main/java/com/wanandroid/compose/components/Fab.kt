package com.wanandroid.compose.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Fab(
    modifier: Modifier = Modifier,
    action: () -> Unit
) {
    FloatingActionButton(
        onClick = action,
        content = {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "",
                tint = Color.White
            )
        },
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomEnd)
            .padding(36.dp)
    )
}