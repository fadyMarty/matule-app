package com.fadymarty.matule.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.presentation.components.LoadingScreen
import com.fadymarty.matule.presentation.components.ProductModal
import com.fadymarty.matule.presentation.home.components.NewsCard
import com.fadymarty.ui_kit.common.theme.MatuleTheme
import com.fadymarty.ui_kit.presentation.components.buttons.ChipButton
import com.fadymarty.ui_kit.presentation.components.cards.PrimaryCard
import com.fadymarty.ui_kit.presentation.components.input.SearchInput
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            SearchInput(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .statusBarsPadding()
                    .padding(top = 24.dp, bottom = 8.dp),
                value = state.searchQuery,
                onValueChange = {
                    onEvent(HomeEvent.SearchQueryChanged(it))
                },
                onClearClick = {
                    onEvent(HomeEvent.SearchQueryChanged(""))
                },
                hint = "Искать  описания"
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingScreen(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                )
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
                contentPadding = PaddingValues(
                    top = 24.dp,
                    bottom = 16.dp
                )
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = "Акции и новости",
                        style = MatuleTheme.typography.title3Semibold,
                        color = MatuleTheme.colorScheme.placeholder
                    )
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(state.news) { index, news ->
                            NewsCard(
                                news = news,
                                index = index
                            )
                        }
                    }
                    Spacer(Modifier.height(32.dp))
                }
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 21.dp),
                        text = "Каталог описаний",
                        style = MatuleTheme.typography.title3Semibold,
                        color = MatuleTheme.colorScheme.placeholder
                    )
                    Spacer(Modifier.height(15.dp))
                }
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            ChipButton(
                                selected = state.type == null,
                                onClick = {
                                    onEvent(HomeEvent.TypeSelected(null))
                                },
                                label = "Все"
                            )
                        }
                        items(state.types) { type ->
                            ChipButton(
                                selected = type == state.type,
                                onClick = {
                                    onEvent(HomeEvent.TypeSelected(type))
                                },
                                label = type
                            )
                        }
                    }
                    Spacer(Modifier.height(25.dp))
                }
                items(state.currentProducts) { product ->
                    PrimaryCard(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        title = product.title,
                        type = product.type,
                        price = "${product.price} ₽",
                        onClick = {
                            onEvent(HomeEvent.ShowProductModal(product))
                        },
                        onButtonClick = {
                            onEvent(HomeEvent.AddProductToCart(product))
                        },
                        added = state.carts.any { it.productId == product.id }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }

    state.product?.let { product ->
        ProductModal(
            product = product,
            onDismissRequest = {
                onEvent(HomeEvent.HideProductModal)
            },
            onAddProductToCart = {
                onEvent(HomeEvent.AddProductToCart(product))
            }
        )
    }
}