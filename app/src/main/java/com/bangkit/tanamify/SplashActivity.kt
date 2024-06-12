package com.bangkit.tanamify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.tanamify.ui.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        val background = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
