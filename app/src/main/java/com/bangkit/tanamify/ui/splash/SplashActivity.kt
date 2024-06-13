package com.bangkit.tanamify.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.tanamify.MainActivity
import com.bangkit.tanamify.R
import com.bangkit.tanamify.ui.login.LoginActivity
import com.bangkit.tanamify.utils.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val viewModelFactory = ViewModelFactory.getInstance(this)
        splashViewModel = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]

        splashViewModel.getSession().observe(this) { userModel ->
            if (userModel.token.isNotEmpty()) {
                navigateToMainActivity()
            } else {
                navigateToLoginActivity()
            }
        }

        supportActionBar?.hide()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
