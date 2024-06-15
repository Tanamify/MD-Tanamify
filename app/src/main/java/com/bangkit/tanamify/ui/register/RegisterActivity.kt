package com.bangkit.tanamify.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bangkit.tanamify.data.retrofit.response.RegisterResponse
import com.bangkit.tanamify.data.state.ResultState
import com.bangkit.tanamify.databinding.ActivityRegisterBinding
import com.bangkit.tanamify.ui.login.LoginActivity
import com.bangkit.tanamify.utils.ViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(this)
        registerViewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            val name = binding.tvInputName.text.toString()
            val email = binding.tvInputEmail.text.toString()
            val password = binding.tvInputPassword.text.toString()
            lifecycleScope.launch {
                registerViewModel.userRegister(name, email, password).collect { result ->
                    handleRegisterResult(result)
                }
            }
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleRegisterResult(result: ResultState<RegisterResponse>) {
        when (result) {
            is ResultState.Success -> {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            is ResultState.Error -> {
                Toast.makeText(this, "Register Failed: ${result.error}", Toast.LENGTH_LONG).show()
            }

            is ResultState.Loading -> {
                Toast.makeText(this, "Registering...", Toast.LENGTH_SHORT).show()
            }
        }

    }


}
