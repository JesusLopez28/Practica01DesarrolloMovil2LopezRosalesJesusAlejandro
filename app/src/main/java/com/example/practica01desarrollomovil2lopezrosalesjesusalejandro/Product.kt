package com.example.practica01desarrollomovil2lopezrosalesjesusalejandro

import java.io.Serializable

class Product(
    var id: Int = 0,
    var name: String = "",
    var price: Double = 0.0,
    var quantity: Int = 0,
    var description: String = ""
) : Serializable
