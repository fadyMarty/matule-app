package com.fadymarty.matule.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.fadymarty.network.domain.model.News
import com.fadymarty.ui_kit.common.theme.MatuleTheme

@Composable
fun NewsCard(
    modifier: Modifier = Modifier,
    news: News,
    index: Int
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .size(270.dp, 152.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = if (index % 2 == 0) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF97D9F0),
                            Color(0xFF92E9D4)
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF76B3FF),
                            Color(0xFFCDE3FF)
                        )
                    )
                }
            )
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(context)
                .data(news.newsImage)
                .crossfade(true)
                .build(),
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = news.id,
                style = MatuleTheme.typography.title2ExtraBold,
                color = MatuleTheme.colorScheme.onAccent
            )
            Text(
                text = news.id,
                style = MatuleTheme.typography.title2ExtraBold,
                color = MatuleTheme.colorScheme.onAccent
            )
        }
    }
}