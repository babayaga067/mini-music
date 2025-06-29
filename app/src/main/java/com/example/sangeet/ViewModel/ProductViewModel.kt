package com.example.sangeet.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sangeet.model.ProductModel
import com.example.sangeet.repository.ProductRepository

class ProductViewModel(private val repo: ProductRepository) : ViewModel() {

    // LiveData for all products
    private val _allProducts = MutableLiveData<List<ProductModel?>>()
    val allProducts: LiveData<List<ProductModel?>> get() = _allProducts

    // LiveData for single product
    private val _product = MutableLiveData<ProductModel?>()
    val product: LiveData<ProductModel?> get() = _product

    // LiveData for loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // LiveData for error messages
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Add a new product
    fun addProduct(product: ProductModel, callback: (Boolean, String) -> Unit) {
        _isLoading.value = true
        repo.addProduct(product) { success, message ->
            _isLoading.value = false
            if (success) {
                // Refresh the products list after successful addition
                getAllProducts()
            } else {
                _errorMessage.value = message
            }
            callback(success, message)
        }
    }

    // Update product by ID with new data
    fun updateProduct(
        productId: String,
        updatedData: Map<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        _isLoading.value = true
        repo.updateProduct(productId, updatedData) { success, message ->
            _isLoading.value = false
            if (success) {
                // Refresh the products list after successful update
                getAllProducts()
                // Also refresh the single product if it's the same ID
                getProductById(productId)
            } else {
                _errorMessage.value = message
            }
            callback(success, message)
        }
    }

    // Delete product by ID
    fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        _isLoading.value = true
        repo.deleteProduct(productId) { success, message ->
            _isLoading.value = false
            if (success) {
                // Refresh the products list after successful deletion
                getAllProducts()
                // Clear the single product if it was the deleted one
                _product.value = null
            } else {
                _errorMessage.value = message
            }
            callback(success, message)
        }
    }

    // Get all products
    fun getAllProducts(callback: ((Boolean, String, List<ProductModel>?) -> Unit)? = null) {
        _isLoading.value = true
        repo.getAllProducts { success, message, products ->
            _isLoading.value = false
            if (success) {
                _allProducts.postValue(products ?: emptyList())
            } else {
                _allProducts.postValue(emptyList())
                _errorMessage.value = message
            }
            callback?.invoke(success, message, products)
        }
    }

    // Get product by ID
    fun getProductById(
        productId: String,
        callback: ((Boolean, String, ProductModel?) -> Unit)? = null
    ) {
        _isLoading.value = true
        repo.getProductById(productId) { success, message, product ->
            _isLoading.value = false
            if (success) {
                _product.postValue(product)
            } else {
                _product.postValue(null)
                _errorMessage.value = message
            }
            callback?.invoke(success, message, product)
        }
    }
//
//    // Search products by name or other criteria
//    fun searchProducts(
//        query: String,
//        callback: ((Boolean, String, List<ProductModel>?) -> Unit)? = null
//    ) {
//        _isLoading.value = true
//        repo.searchProducts(query) { success, message, products ->
//            _isLoading.value = false
//            if (success) {
//                _allProducts.postValue(products ?: emptyList())
//            } else {
//                _allProducts.postValue(emptyList())
//                _errorMessage.value = message
//            }
//            callback?.invoke(success, message, products)
//        }
//    }
//
//    // Get products by category
//    fun getProductsByCategory(
//        category: String,
//        callback: ((Boolean, String, List<ProductModel>?) -> Unit)? = null
//    ) {
//        _isLoading.value = true
//        repo.getProductsByCategory(category) { success, message, products ->
//            _isLoading.value = false
//            if (success) {
//                _allProducts.postValue(products ?: emptyList())
//            } else {
//                _allProducts.postValue(emptyList())
//                _errorMessage.value = message
//            }
//            callback?.invoke(success, message, products)
//        }
//    }

//    // Clear error message
//    fun clearErrorMessage() {
//        _errorMessage.value = null
//    }

    // Clear product data
    fun clearProductData() {
        _product.value = null
    }

    // Clear all products data
    fun clearAllProductsData() {
        _allProducts.value = emptyList()
    }

    // Refresh data
    fun refreshData() {
        getAllProducts()
    }

    // Check if products list is empty
    fun isProductsEmpty(): Boolean {
        return _allProducts.value?.isEmpty() ?: true
    }

    // Get products count
    fun getProductsCount(): Int {
        return _allProducts.value?.size ?: 0
    }
}