package com.example.cleanforecast


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val btnForecast = findViewById<Button>(R.id.btnForecast)
        val etLocation = findViewById<TextInputEditText>(R.id.etLocation)
        btnForecast.setOnClickListener {
            val location = etLocation.text.toString()
            val i = Intent(this, Home::class.java).apply {
                putExtra("Data",location)
            }
            startActivity(i)
        }
    }
}