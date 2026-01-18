package com.fadymarty.matule.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.fadymarty.ui_kit.common.theme.MatuleTheme

@Composable
fun PinNumber(
    number: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(
                if (selected) {
                    MatuleTheme.colorScheme.accent
                } else MatuleTheme.colorScheme.inputBg
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MatuleTheme.typography.title1Semibold,
            color = if (selected) {
                MatuleTheme.colorScheme.onAccent
            } else MatuleTheme.colorScheme.onBackground
        )
    }
}