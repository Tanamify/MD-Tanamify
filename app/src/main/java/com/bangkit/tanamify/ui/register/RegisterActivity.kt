package com.bangkit.tanamify.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.tanamify.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val name = binding.tvInputName.text.toString().trim()
            val email = binding.tvInputEmail.text.toString().trim()
            val password = binding.tvInputPassword.text.toString().trim()

            if (validateInputs(name, email, password)) {
                Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            Toast.makeText(this, "Navigate to Login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(name: String, email: String, password: String): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }
}
