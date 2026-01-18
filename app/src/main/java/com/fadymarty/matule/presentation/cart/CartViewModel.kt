package com.fadymarty.matule.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule.common.util.Constants
import com.fadymarty.matule.common.util.MainSnackbarController
import com.fadymarty.matule.common.util.SnackbarController
import com.fadymarty.matule.common.util.SnackbarEvent
import com.fadymarty.network.domain.model.Cart
import com.fadymarty.network.domain.use_case.cart.DeleteCartUseCase
import com.fadymarty.network.domain.use_case.cart.GetCartsUseCase
import com.fadymarty.network.domain.use_case.cart.ObserveCartsUseCase
import com.fadymarty.network.domain.use_case.cart.UpdateCartUseCase
import com.fadymarty.network.domain.use_case.order.CreateOrderUseCase
import com.fadymarty.network.domain.use_case.shop.GetProductsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val observeCartsUseCase: ObserveCartsUseCase,
    private val getCartsUseUse: GetCartsUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<CartEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getProductsUseCase()
                .onSuccess { products ->
                    _state.update {
                        it.copy(products = products)
                    }
                }
                .onFailure {
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(Constants.ERROR_MESSAGE)
                    )
                }
            getCartsUseUse()
                .onFailure {
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(Constants.ERROR_MESSAGE)
                    )
                }
            _state.update { it.copy(isLoading = false) }
        }

        observeCartsUseCase().onEach { carts ->
            _state.update { it.copy(carts = carts) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.DeleteCart -> {
                deleteCart(event.cart)
            }

            is CartEvent.UpdateCart -> {
                updateCart(event.cart)
            }

            is CartEvent.ClearCart -> {
                clearCart()
            }

            is CartEvent.CreateOrder -> {
                createOrder()
            }

            CartEvent.NavigateBack -> {
                viewModelScope.launch {
                    eventChannel.send(CartEvent.NavigateBack)
                }
            }

            else -> Unit
        }
    }

    private fun createOrder() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            _state.value.carts.forEach { cart ->
                createOrderUseCase(cart)
                    .onSuccess {
                        eventChannel.send(CartEvent.NavigateToMainGraph)
                        MainSnackbarController.sendEvent(
                            event = SnackbarEvent(Constants.SUCCESS_ORDER_MESSAGE)
                        )
                    }
                    .onFailure {
                        SnackbarController.sendEvent(
                            event = SnackbarEvent(Constants.ERROR_MESSAGE)
                        )
                    }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun clearCart() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            _state.value.carts.forEach { cart ->
                deleteCartUseCase(cart.id!!)
                    .onFailure {
                        SnackbarController.sendEvent(
                            event = SnackbarEvent(Constants.ERROR_MESSAGE)
                        )
                    }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun updateCart(cart: Cart) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            updateCartUseCase(cart)
                .onFailure {
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(Constants.ERROR_MESSAGE)
                    )
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun deleteCart(cart: Cart) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            deleteCartUseCase(cart.id!!)
                .onFailure {
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(Constants.ERROR_MESSAGE)
                    )
                }
            _state.update { it.copy(isLoading = false) }
        }
    }
}