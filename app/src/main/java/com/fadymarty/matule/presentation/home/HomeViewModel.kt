package com.fadymarty.matule.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule_network.domain.model.Product
import com.fadymarty.matule_network.domain.use_case.cart.AddProductToCartUseCase
import com.fadymarty.matule_network.domain.use_case.cart.DeleteCartUseCase
import com.fadymarty.matule_network.domain.use_case.cart.GetCartsUseCase
import com.fadymarty.matule_network.domain.use_case.cart.ObserveCartsUseCase
import com.fadymarty.matule_network.domain.use_case.shop.GetNewsUseCase
import com.fadymarty.matule_network.domain.use_case.shop.GetProductsUseCase
import com.fadymarty.matule_network.domain.use_case.shop.SearchProductsUseCase
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
    private val deleteCartUseCase: DeleteCartUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val news = async { getNewsUseCase() }
            val products = async { getProductsUseCase() }
            val carts = async { getCartsUseCase() }

            val newsResult = news.await()
                .onSuccess { news ->
                    _state.update { it.copy(news = news) }
                }
            val productsResult = products.await()
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
            val cartsResult = carts.await()

            val results = listOf(newsResult, productsResult, cartsResult)
            if (results.any { it.isSuccess }) {
                _state.update { it.copy(isLoading = false) }
            } else {
                eventChannel.send(HomeEvent.ShowErrorSnackBar)
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

            is HomeEvent.SelectType -> {
                selectType(event.type)
            }

            is HomeEvent.ShowProductModal -> {
                _state.update { it.copy(product = event.product) }
            }

            HomeEvent.HideProductModal -> {
                _state.update { it.copy(product = null) }
            }

            is HomeEvent.AddProductToCart -> {
                addProductToCart(event.product)
            }

            else -> Unit
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
                            currentProducts = if (_state.value.type != null) {
                                products
                                    .filter { product ->
                                        product.typeCloses == _state.value.type
                                    }
                            } else products
                        )
                    }
                }
                .onFailure {
                    eventChannel.send(HomeEvent.ShowErrorSnackBar)
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun selectType(type: String?) {
        _state.update { it.copy(type = type) }
        _state.update {
            it.copy(
                currentProducts = if (type != null) {
                    it.products.filter { product ->
                        product.typeCloses == type
                    }
                } else it.products
            )
        }
    }

    private fun addProductToCart(product: Product) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, product = null) }
            val cart = _state.value.carts.firstOrNull { it.productId == product.id }
            if (cart != null) {
                deleteCartUseCase(cart.id!!)
                    .onFailure {
                        eventChannel.send(HomeEvent.ShowErrorSnackBar)
                    }
            } else {
                addProductToCartUseCase(product)
                    .onFailure {
                        eventChannel.send(HomeEvent.ShowErrorSnackBar)
                    }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}