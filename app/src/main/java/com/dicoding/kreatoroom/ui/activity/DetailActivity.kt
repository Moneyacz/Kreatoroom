package com.dicoding.kreatoroom.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.kreatoroom.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}