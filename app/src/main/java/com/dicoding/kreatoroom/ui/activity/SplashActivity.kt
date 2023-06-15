package com.dicoding.kreatoroom.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.kreatoroom.databinding.ActivitySplashBinding
import com.dicoding.kreatoroom.ui.viewmodel.LoginViewModel
import com.dicoding.kreatoroom.ui.viewmodel.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val loginView: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        delay()

        userLogCheck()

    }

    private fun userLogCheck() {
        loginView.getUser.observe(this) { token ->
            if ((token != "")) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.putExtra(MainActivity.TOKEN, token)
                startActivity(intent)
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun delay() {
        Thread{
            var counting = 0
            while(counting <= 100) {
                counting += 10
                Thread.sleep(300L)
            }
        }.start()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 4000L)
    }
}
