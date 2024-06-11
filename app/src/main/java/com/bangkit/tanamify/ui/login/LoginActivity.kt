package com.bangkit.tanamify.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.tanamify.R
import com.bangkit.tanamify.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener{
            val name = binding.tvInputName.text.toString().trim()
            val password = binding.tvInputPassword.text.toString().trim()

            if (validateInputs(name, password)) {
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnRegister.setOnClickListener{
            Toast.makeText(this, "Register Clicked", Toast.LENGTH_SHORT).show()
        }
    }
    private fun validateInputs(name: String, password: String): Boolean {
        return name.isNotEmpty() && password.isNotEmpty()
    }
}

