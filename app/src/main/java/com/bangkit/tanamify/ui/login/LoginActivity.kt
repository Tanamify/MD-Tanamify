package com.bangkit.tanamify.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bangkit.tanamify.MainActivity
import com.bangkit.tanamify.R
import com.bangkit.tanamify.data.api.ApiConfig
import com.bangkit.tanamify.data.di.Injection
import com.bangkit.tanamify.data.pref.UserModel
import com.bangkit.tanamify.data.retrofit.response.LoginResponse
import com.bangkit.tanamify.data.state.ResultState
import com.bangkit.tanamify.databinding.ActivityLoginBinding
import com.bangkit.tanamify.ui.register.RegisterActivity
import com.bangkit.tanamify.utils.ToastUtils
import com.bangkit.tanamify.utils.ViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val viewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val email = binding.tvInputEmail.text.toString()
            val password = binding.tvInputPassword.text.toString()
            lifecycleScope.launch {
                loginViewModel.userLogin(email, password).collect { result ->
                    handleLoginResult(result, email)
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            binding.tvInputPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.btnTogglePassword.setImageResource(R.drawable.baseline_visibility_off_24)
        } else {
            binding.tvInputPassword.inputType = InputType.TYPE_CLASS_TEXT
            binding.btnTogglePassword.setImageResource(R.drawable.baseline_visibility_24)
        }
        isPasswordVisible = !isPasswordVisible
        binding.tvInputPassword.setSelection(binding.tvInputPassword.text.length)
    }

    private fun handleLoginResult(result: ResultState<LoginResponse>, email: String) {
        when (result) {
            is ResultState.Success -> {
                val token = result.data.token
                val userId = result.data.userId
                Log.d("LoginActivity", "Received Token: $token")
                saveTokenToSharedPreferences(token, userId)
                val userModel = UserModel(email, token, isLogin = true)
                val userRepository = Injection.provideRepository(applicationContext)
                userRepository.updateApiService(ApiConfig.getApiService(token))
                loginViewModel.saveSession(userModel)
                ToastUtils.showToast(this, "Login Berhasil")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            is ResultState.Error -> {
                ToastUtils.showToast(this, "Login Gagal: ${result.error}")
            }
            is ResultState.Loading -> {}
        }
    }

    private fun saveTokenToSharedPreferences(token: String, userId: String) {
        val sharedPreferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("USER_TOKEN", token).apply()
        sharedPreferences.edit().putString("USER_ID", userId).apply()
        Log.d("LoginActivity", "Token and UserId Saved: $token, $userId")
    }

}
