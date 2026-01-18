package com.fadymarty.matule.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule.common.util.Constants
import com.fadymarty.matule.common.util.MainSnackbarController
import com.fadymarty.matule.common.util.SnackbarEvent
import com.fadymarty.network.domain.model.Product
import com.fadymarty.network.domain.use_case.cart.AddProductToCartUseCase
import com.fadymarty.network.domain.use_case.cart.DeleteCartUseCase
import com.fadymarty.network.domain.use_case.cart.GetCartsUseCase
import com.fadymarty.network.domain.use_case.cart.ObserveCartsUseCase
import com.fadymarty.network.domain.use_case.shop.GetNewsUseCase
import com.fadymarty.network.domain.use_case.shop.GetProductsUseCase
import com.fadymarty.network.domain.use_case.shop.SearchProductsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getCartsUseCase: GetCartsUseCase,
    private val observeCartsUseCase: ObserveCartsUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val newsDeferred = async { getNewsUseCase() }
            val productsDeferred = async { getProductsUseCase() }
            val cartsDeferred = async { getCartsUseCase() }

            val newsResult = newsDeferred.await()
                .onSuccess { news ->
                    _state.update { it.copy(news = news) }
                }
            val productsResult = productsDeferred.await()
                .onSuccess { products ->
                    _state.update {
                        it.copy(
                            products = products,
                            currentProducts = products,
                            types = products.map { product ->
                                product.typeCloses
                            }.distinct()
                        )
                    }
                }
            val cartsResult = cartsDeferred.await()

            val results = listOf(newsResult, productsResult, cartsResult)
            if (results.any { it.isSuccess }) {
                _state.update { it.copy(isLoading = false) }
            } else {
                MainSnackbarController.sendEvent(
                    event = SnackbarEvent(Constants.ERROR_MESSAGE)
                )
            }
        }

        observeCartsUseCase().onEach { carts ->
            _state.update { it.copy(carts = carts) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchQueryChanged -> {
                searchProducts(event.query)
            }

            is HomeEvent.TypeSelected -> {
                _state.update {
                    it.copy(
                        type = event.type,
                        currentProducts = if (event.type != null) {
                            it.products.filter { product ->
                                product.typeCloses == event.type
                            }
                        } else it.products
                    )
                }
            }

            is HomeEvent.AddProductToCart -> {
                addProductToCart(event.product)
            }

            is HomeEvent.ShowProductModal -> {
                _state.update { it.copy(product = event.product) }
            }

            HomeEvent.HideProductModal -> {
                _state.update { it.copy(product = null) }
            }
        }
    }

    private fun searchProducts(query: String) {
        _state.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            _state.update { it.copy(isLoading = true) }
            searchProductsUseCase(query)
                .onSuccess { products ->
                    _state.update {
                        it.copy(
                            products = products,
                            types = products.map { product ->
                                product.typeCloses
                            }.distinct(),
                            currentProducts = if (it.type != null) {
                                products.filter { product ->
                                    product.typeCloses == it.type
                                }
                            } else products
                        )
                    }
                }
                .onFailure {
                    MainSnackbarController.sendEvent(
                        event = SnackbarEvent(Constants.ERROR_MESSAGE)
                    )
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun addProductToCart(product: Product) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, product = null) }
            val cart = _state.value.carts.firstOrNull { it.productId == product.id }
            if (cart != null) {
                deleteCartUseCase(cart.id!!)
                    .onFailure {
                        MainSnackbarController.sendEvent(
                            event = SnackbarEvent(Constants.ERROR_MESSAGE)
                        )
                    }
            } else {
                addProductToCartUseCase(product)
                    .onFailure {
                        MainSnackbarController.sendEvent(
                            event = SnackbarEvent(Constants.ERROR_MESSAGE)
                        )
                    }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}