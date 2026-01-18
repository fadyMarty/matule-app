package com.fadymarty.matule.presentation.cart

import com.fadymarty.network.domain.model.Cart
import com.fadymarty.network.domain.model.Product

data class CartState(
    val isLoading: Boolean = true,
    val carts: List<Cart> = emptyList(),
    val products: List<Product> = emptyList(),
)
