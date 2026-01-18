package com.fadymarty.matule.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.fadymarty.matule.R
import com.fadymarty.ui_kit.common.theme.MatuleTheme

@Composable
fun PinScreen(
    pin: String,
    onNumberClick: (Int) -> Unit,
    onRemoveLastClick: () -> Unit,
    selectedNumber: Int?,
    title: String,
    subtitle: String = "",
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding() + 100.dp,
                    bottom = innerPadding.calculateBottomPadding() + 80.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MatuleTheme.typography.title1ExtraBold
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = subtitle,
                style = MatuleTheme.typography.textRegular,
                color = MatuleTheme.colorScheme.placeholder
            )
            Spacer(Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(4) { index ->
                    if (index < pin.length) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(MatuleTheme.colorScheme.accent)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 1.dp,
                                    color = MatuleTheme.colorScheme.accent,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
            Spacer(Modifier.height(60.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                maxItemsInEachRow = 3
            ) {
                repeat(12) { index ->
                    val number = if (index == 10) 0 else index + 1
                    when (index) {
                        9 -> {
                            Spacer(Modifier.size(80.dp))
                        }

                        11 -> {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        onRemoveLastClick()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(35.dp, 24.dp),
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_del),
                                    contentDescription = null
                                )
                            }
                        }

                        else -> {
                            PinNumber(
                                number = number,
                                selected = number == selectedNumber,
                                onClick = {
                                    onNumberClick(number)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}