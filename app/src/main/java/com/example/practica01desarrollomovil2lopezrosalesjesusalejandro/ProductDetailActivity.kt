package com.example.practica01desarrollomovil2lopezrosalesjesusalejandro

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var ivProductImage: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvProductDescription: TextView
    private lateinit var btnAddToCart: Button
    private lateinit var btnBack: ImageButton
    private lateinit var cart: Cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        ivProductImage = findViewById(R.id.ivProductImage)
        tvProductName = findViewById(R.id.tvProductName)
        tvProductPrice = findViewById(R.id.tvProductPrice)
        tvProductDescription = findViewById(R.id.tvProductDescription)
        btnAddToCart = findViewById(R.id.btnAddToCart)
        btnBack = findViewById(R.id.btnBack)

        cart = Cart(this)

        createNotificationChannel()

        val product = intent.getSerializableExtra("PRODUCT") as? Product

        product?.let {
            tvProductName.text = it.name
            tvProductPrice.text = "$${it.price}"
            tvProductDescription.text = it.description

            ivProductImage.setImageResource(getProductImageResource(it.id))
        }

        btnAddToCart.setOnClickListener {
            product?.let {
                cart.addProduct(it)
                sendCartNotification(it)
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getProductImageResource(id: Int): Int {
        return when (id) {
            1 -> R.drawable.silla
            2 -> R.drawable.mesa
            3 -> R.drawable.sillon
            4 -> R.drawable.cama
            5 -> R.drawable.escritorio
            6 -> R.drawable.librero
            7 -> R.drawable.silla_oficina
            8 -> R.drawable.silla_gamer
            9 -> R.drawable.silla_ruedas
            10 -> R.drawable.silla_playa
            else -> R.drawable.default_product_image
        }
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Cart Notifications"
            val descriptionText = "Notifications for cart actions"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("cart_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendCartNotification(product: Product) {
        val checkoutIntent = Intent(this, CheckoutActivity::class.java)
        val checkoutPendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            checkoutIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val yesIntent = Intent(this, CheckoutActivity::class.java)
        val yesPendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            1,
            yesIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val noIntent = Intent(this, HomeActivity::class.java)
        val noPendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            2,
            noIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "cart_channel")
            .setSmallIcon(R.drawable.ic_cart)
            .setContentTitle("Producto agregado al carrito")
            .setContentText("¿Desea ir a finalizar su compra?")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(checkoutPendingIntent)
            .addAction(R.drawable.ic_check, "Sí", yesPendingIntent)
            .addAction(R.drawable.ic_close, "No", noPendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@ProductDetailActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@ProductDetailActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
                return
            }
            notify(1001, notification)
        }
    }

}
