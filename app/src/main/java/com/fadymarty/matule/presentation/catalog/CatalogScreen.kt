package com.fadymarty.matule.presentation.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.components.LoadingScreen
import com.fadymarty.matule.presentation.components.ProductModal
import com.fadymarty.matule.common.util.ObserveAsEvents
import com.fadymarty.ui_kit.presentation.components.buttons.CartButton
import com.fadymarty.ui_kit.presentation.components.buttons.ChipButton
import com.fadymarty.ui_kit.presentation.components.cards.PrimaryCard
import com.fadymarty.ui_kit.presentation.components.input.SearchInput
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CatalogRoot(
    onNavigateToProfile: () -> Unit,
    onNavigateToCart: () -> Unit,
    viewModel: CatalogViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CatalogEvent.NavigateToProfile -> {
                onNavigateToProfile()
            }

            CatalogEvent.NavigateToCart -> {
                onNavigateToCart()
            }

            else -> Unit
        }
    }

    CatalogScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun CatalogScreen(
    state: CatalogState,
    onEvent: (CatalogEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(top = 28.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(38.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchInput(
                    modifier = Modifier.weight(1f),
                    value = state.searchQuery,
                    onValueChange = {
                        onEvent(CatalogEvent.SearchQueryChanged(it))
                    },
                    onClearClick = {
                        onEvent(CatalogEvent.SearchQueryChanged(""))
                    },
                    hint = "Искать описания"
                )
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            onEvent(CatalogEvent.NavigateToProfile)
                        },
                    painter = painterResource(R.drawable.img_user_icon),
                    contentDescription = null,
                )
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingScreen(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                )
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    )
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ChipButton(
                            selected = state.type == null,
                            onClick = {
                                onEvent(CatalogEvent.TypeSelected(null))
                            },
                            label = "Все"
                        )
                    }
                    items(state.types) { type ->
                        ChipButton(
                            selected = type == state.type,
                            onClick = {
                                onEvent(CatalogEvent.TypeSelected(type))
                            },
                            label = type
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        bottom = 8.dp,
                        end = 20.dp
                    )
                ) {
                    items(state.currentProducts) { product ->
                        PrimaryCard(
                            title = product.title,
                            type = product.type,
                            price = "${product.price} ₽",
                            onClick = {
                                onEvent(CatalogEvent.ShowProductModal(product))
                            },
                            onButtonClick = {
                                onEvent(CatalogEvent.AddProductToCart(product))
                            },
                            added = state.carts.any { it.productId == product.id }
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
                if (state.carts.isNotEmpty()) {
                    CartButton(
                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            vertical = 32.dp
                        ),
                        onClick = {
                            onEvent(CatalogEvent.NavigateToCart)
                        },
                        price = state.products.filter { product ->
                            state.carts.any { cart ->
                                cart.productId == product.id
                            }
                        }.sumOf { product ->
                            product.price * state.carts.first { cart ->
                                cart.productId == product.id
                            }.count
                        }
                    )
                }
            }
        }
    }

    state.product?.let { product ->
        ProductModal(
            product = product,
            onDismissRequest = {
                onEvent(CatalogEvent.HideProductModal)
            },
            onAddProductToCart = {
                onEvent(CatalogEvent.AddProductToCart(product))
            }
        )
    }
}