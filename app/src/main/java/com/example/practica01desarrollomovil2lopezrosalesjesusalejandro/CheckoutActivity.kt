package com.example.practica01desarrollomovil2lopezrosalesjesusalejandro

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CheckoutActivity : AppCompatActivity() {

    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvTotalAmount: TextView
    private lateinit var btnCheckout: Button
    private lateinit var btnBack: ImageButton

    private lateinit var cart: Cart
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        rvCartItems = findViewById(R.id.rvCartItems)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        btnCheckout = findViewById(R.id.btnCheckout)
        btnBack = findViewById(R.id.btnBack)

        cart = Cart(this)
        val cartItems = cart.getCart()

        rvCartItems.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(cartItems)
        rvCartItems.adapter = cartAdapter

        val totalAmount = cartItems.sumOf { it.price }
        tvTotalAmount.text = "Total: $${"%.2f".format(totalAmount)}"

        createNotificationChannel()

        btnCheckout.setOnClickListener {
            Toast.makeText(this, "Compra finalizada!", Toast.LENGTH_SHORT).show()
            cart.clearCart()
            sendPurchaseNotification()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Compra Finalizada"
            val descriptionText = "Notificaciones de compra finalizada"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("purchase_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendPurchaseNotification() {
        val intent = Intent(this, HomeActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "purchase_channel")
            .setSmallIcon(R.drawable.ic_purchase)
            .setContentTitle("Compra exitosa")
            .setContentText("Tu compra ha sido procesada correctamente")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@CheckoutActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@CheckoutActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
                return
            }
            notify(1002, notification)
        }
    }
}
