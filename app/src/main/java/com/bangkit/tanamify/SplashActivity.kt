package com.bangkit.tanamify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.tanamify.data.pref.UserPreference
import com.bangkit.tanamify.data.pref.UserModel
import com.bangkit.tanamify.data.pref.dataStore
import com.bangkit.tanamify.ui.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        lifecycleScope.launch {
            val userPref = UserPreference.getInstance(dataStore)
            val user: UserModel = userPref.getSession().first()
            if (user.isLogin) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }
}
