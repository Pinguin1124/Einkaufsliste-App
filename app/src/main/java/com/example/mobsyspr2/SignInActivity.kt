package com.example.mobsyspr2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobsyspr2.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = Firebase.auth

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (checkAllField()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Login successful, redirect to HomeActivity
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If login fails, display a message to the user.
                            Toast.makeText(baseContext, "Login failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun checkAllField(): Boolean {
        val email = binding.etEmail.text.toString()
        if (email.isEmpty()) {
            binding.textInputLayoutEmail.error = "This is required field"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }
        if (binding.etPassword.text.toString().isEmpty()) {
            binding.textInputLayoutPassword.error = "This is required field"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if (binding.etPassword.text.toString().length < 6) {
            binding.textInputLayoutPassword.error = "Password must be at least 6 characters"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        return true
    }
}