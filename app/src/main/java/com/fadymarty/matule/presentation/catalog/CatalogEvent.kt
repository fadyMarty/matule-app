package com.fadymarty.matule.presentation.catalog

import com.fadymarty.network.domain.model.Product

sealed interface CatalogEvent {
    data class SearchQueryChanged(val query: String) : CatalogEvent
    data class TypeSelected(val type: String?) : CatalogEvent
    data class ShowProductModal(val product: Product?) : CatalogEvent
    data object HideProductModal : CatalogEvent
    data class AddProductToCart(val product: Product) : CatalogEvent
    data object NavigateToProfile : CatalogEvent
    data object NavigateToCart : CatalogEvent
}