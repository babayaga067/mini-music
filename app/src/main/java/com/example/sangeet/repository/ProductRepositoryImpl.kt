package com.example.sangeet.repository

import com.example.sangeet.model.ProductModel
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class ProductRepositoryImpl : ProductRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("products")

    override fun addProduct(product: ProductModel, callback: (Boolean, String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .document(product.productId)
            .set(product)
            .addOnSuccessListener {
                callback(true, "Product added successfully!")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Error adding product")
            }
    }

    override fun updateProduct(
        productId: String,
        updatedData: Map<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(productId).updateChildren(updatedData).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product updated successfully!")
            } else {
                callback(false, it.exception?.message ?: "Failed to update product.")
            }
        }
    }

    override fun deleteProduct(
        productId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(productId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product deleted successfully!")
            } else {
                callback(false, it.exception?.message ?: "Failed to delete product.")
            }
        }
    }

    override fun getAllProducts(callback: (Boolean, String, List<ProductModel>?) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var allProducts = mutableListOf<ProductModel>()
                    for (eachProduct in snapshot.children) {
                        var products = eachProduct.getValue(ProductModel:: class.java)
                        if (products != null) {
                            allProducts. add (products)
                        }
                    }
                    callback(true, "product fetched successfully", allProducts)
                }
            }


            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }

    override fun getProductById(
        productId: String,
        callback: (Boolean, String, ProductModel?) -> Unit
    ) {
        ref.child(productId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(ProductModel::class.java)
                if (product != null) {
                    callback(true, "Product fetched successfully!", product)
                } else {
                    callback(false, "Product not found", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }
}
