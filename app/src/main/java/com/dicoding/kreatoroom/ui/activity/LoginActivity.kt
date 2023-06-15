package com.dicoding.kreatoroom.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.kreatoroom.utils.Result
import com.dicoding.kreatoroom.databinding.ActivityLoginBinding
import com.dicoding.kreatoroom.ui.viewmodel.LoginViewModel
import com.dicoding.kreatoroom.ui.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        start()
        validate()

    }

    private fun validate() {
        viewModel.login.observe(this) { ans ->
            when (ans) {
                is Result.Loading -> {

                }
                is Result.Success -> ans.data?.loginResult?.let {
                    viewModel.saveUser(it.token)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra(MainActivity.EX_TOKEN, it.token)
                    startActivity(intent)
                    finish()
                }
                is Result.Error -> ans.data.let {
                    Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun start() {

        binding.buttonLogin.setOnClickListener{
            val mail = binding.editTextUsername.text.toString()
            val pw = binding.editTextPassword.text.toString()

            when {
                mail.isEmpty() -> {
                    binding.editTextUsername.error = getString(R.string.email_none)
                }
                pw.length < 8 -> {
                    binding.editTextPassword.error = getString(R.string.pass_length)
                }
                else -> {
                    val login = RequestLogin(mail, pw)
                    viewModel.login(login)
                }
            }
        }
        binding.toRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}