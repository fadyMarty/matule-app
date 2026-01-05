package com.fadymarty.matule.presentation.home

import com.fadymarty.matule_network.domain.model.Cart
import com.fadymarty.matule_network.domain.model.News
import com.fadymarty.matule_network.domain.model.Product

data class HomeState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val news: List<News> = emptyList(),
    val products: List<Product> = emptyList(),
    val currentProducts: List<Product> = emptyList(),
    val types: List<String> = emptyList(),
    val carts: List<Cart> = emptyList(),
    val type: String? = null,
    val product: Product? = null
)