package com.example.sangeet.repository

import com.example.sangeet.model.ProductModel

interface ProductRepository {

    // Add a new product
    fun addProduct(
        product: ProductModel,
        callback: (Boolean, String) -> Unit
    )

    // Update product by id with new data
    fun updateProduct(
        productId: String,
        updatedData: Map<String, Any?>,
        callback: (Boolean, String) -> Unit
    )

    // Delete product by id
    fun deleteProduct(
        productId: String,
        callback: (Boolean, String) -> Unit
    )

    // Get all products (asynchronously)
    fun getAllProducts(callback: (Boolean, String, List<ProductModel>?) -> Unit)

    // Get product by ID
    fun getProductById(
        productId: String,
        callback: (Boolean, String, ProductModel?) -> Unit
    )
}
