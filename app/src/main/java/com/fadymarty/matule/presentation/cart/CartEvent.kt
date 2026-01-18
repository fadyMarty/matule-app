package com.fadymarty.matule.presentation.cart

import com.fadymarty.network.domain.model.Cart

sealed interface CartEvent {
    data class DeleteCart(val cart: Cart) : CartEvent
    data object ClearCart : CartEvent
    data class UpdateCart(val cart: Cart) : CartEvent
    data object CreateOrder : CartEvent
    data object NavigateBack : CartEvent
    data object NavigateToMainGraph : CartEvent
}