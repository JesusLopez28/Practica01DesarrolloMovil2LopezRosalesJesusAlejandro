package com.example.practica01desarrollomovil2lopezrosalesjesusalejandro

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.Intent

class InformativeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informative)

        val totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)

        val tvInfo = findViewById<TextView>(R.id.tvInfo)
        tvInfo.text = "El total de tu compra fue: $${"%.2f".format(totalAmount)}"

        val btnReturn = findViewById<Button>(R.id.btnReturn)
        btnReturn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
