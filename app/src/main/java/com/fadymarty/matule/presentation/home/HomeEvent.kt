package com.fadymarty.matule.presentation.home

import com.fadymarty.network.domain.model.Product

sealed interface HomeEvent {
    data class SearchQueryChanged(val query: String) : HomeEvent
    data class TypeSelected(val type: String?) : HomeEvent
    data class ShowProductModal(val product: Product?) : HomeEvent
    data object HideProductModal : HomeEvent
    data class AddProductToCart(val product: Product) : HomeEvent
}