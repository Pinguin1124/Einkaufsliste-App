package com.example.mobsyspr2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}

// Achtung! Dieses Kommentar ist zum Auslösen des Workflows... (2)
