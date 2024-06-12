package com.bangkit.tanamify.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bangkit.tanamify.MainActivity
import com.bangkit.tanamify.data.api.ApiConfig
import com.bangkit.tanamify.data.di.Injection
import com.bangkit.tanamify.data.pref.UserModel
import com.bangkit.tanamify.data.retrofit.response.LoginResponse
import com.bangkit.tanamify.data.state.ResultState
import com.bangkit.tanamify.databinding.ActivityLoginBinding
import com.bangkit.tanamify.ui.register.RegisterActivity
import com.bangkit.tanamify.utils.ViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val email = binding.tvInputName.text.toString()
            val password = binding.tvInputPassword.text.toString()
            lifecycleScope.launch {
                loginViewModel.userLogin(email, password).collect { result ->
                    handleLoginResult(result, email)
                }
            }
        }

        binding.btnRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleLoginResult(result: ResultState<LoginResponse>, email: String) {
        when (result) {
            is ResultState.Success -> {
                val token = result.data.token
                val userModel = UserModel(email, token, isLogin = true)
                val userRepository = Injection.provideRepository(applicationContext)
                userRepository.updateApiService(ApiConfig.getApiService(token))
                loginViewModel.saveSession(userModel)
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            is ResultState.Error -> {
                Toast.makeText(this, "Login Failed: ${result.error}", Toast.LENGTH_SHORT).show()
            }

            is ResultState.Loading -> {
            }
        }
    }
}
