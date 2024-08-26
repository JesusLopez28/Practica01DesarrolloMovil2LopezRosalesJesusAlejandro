package com.example.practica01desarrollomovil2lopezrosalesjesusalejandro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rvProducts = findViewById(R.id.rvProducts)
        rvProducts.layoutManager = LinearLayoutManager(this)

        val products = listOf(
            Product(1, "Silla", 200.0, 10, "Una silla cómoda"),
            Product(2, "Mesa", 500.0, 5, "Una mesa grande"),
            Product(3, "Sillón", 1000.0, 3, "Un sillón grande"),
            Product(4, "Cama", 1500.0, 2, "Una cama matrimonial"),
            Product(5, "Escritorio", 800.0, 7, "Un escritorio grande"),
            Product(6, "Librero", 600.0, 4, "Un librero grande"),
            Product(7, "Silla de oficina", 300.0, 6, "Una silla de oficina"),
            Product(8, "Silla gamer", 400.0, 8, "Una silla gamer"),
            Product(9, "Silla de ruedas", 1000.0, 2, "Una silla de ruedas"),
            Product(10, "Silla de playa", 150.0, 9, "Una silla de playa")
        )

        productsAdapter = ProductsAdapter(products) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT", product)
            startActivity(intent)
        }

        rvProducts.adapter = productsAdapter
    }
}
