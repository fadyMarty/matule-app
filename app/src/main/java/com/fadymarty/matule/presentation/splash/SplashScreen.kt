package com.fadymarty.matule.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule.common.util.ObserveAsEvents
import com.fadymarty.ui_kit.common.theme.MatuleTheme
import com.fadymarty.ui_kit.common.theme.RobotoFamily
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashRoot(
    onNavigate: (Route) -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SplashEvent.Navigate -> {
                onNavigate(event.route)
            }
        }
    }

    SplashScreen()
}

@Composable
private fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.img_splash_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = stringResource(R.string.app_name),
            fontFamily = RobotoFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 40.sp,
            lineHeight = 64.sp,
            letterSpacing = 1.04.sp,
            color = MatuleTheme.colorScheme.onAccent
        )
    }
}