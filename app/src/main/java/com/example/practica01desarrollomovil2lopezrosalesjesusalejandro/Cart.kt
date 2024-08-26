package com.example.practica01desarrollomovil2lopezrosalesjesusalejandro

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Cart(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("CART_PREFS", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun addProduct(product: Product) {
        val cartJson = sharedPreferences.getString("CART", "[]")
        val productListType = object : TypeToken<MutableList<Product>>() {}.type
        val productList: MutableList<Product> = gson.fromJson(cartJson, productListType)
        productList.add(product)
        saveCart(productList)
    }

    fun getCart(): List<Product> {
        val cartJson = sharedPreferences.getString("CART", "[]")
        val productListType = object : TypeToken<List<Product>>() {}.type
        return gson.fromJson(cartJson, productListType)
    }

    fun clearCart() {
        sharedPreferences.edit().remove("CART").apply()
    }

    private fun saveCart(productList: List<Product>) {
        val cartJson = gson.toJson(productList)
        sharedPreferences.edit().putString("CART", cartJson).apply()
    }
}
