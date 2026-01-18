package com.fadymarty.matule.presentation.catalog

import com.fadymarty.network.domain.model.Cart
import com.fadymarty.network.domain.model.Product


data class CatalogState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val products: List<Product> = emptyList(),
    val currentProducts: List<Product> = emptyList(),
    val types: List<String> = emptyList(),
    val carts: List<Cart> = emptyList(),
    val type: String? = null,
    val product: Product? = null
)
