package com.dicoding.kreatoroom.summary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.kreatoroom.databinding.ActivityDetailBinding

class SummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}