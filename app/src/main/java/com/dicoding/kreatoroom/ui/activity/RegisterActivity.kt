package com.dicoding.kreatoroom.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyappapi2.databinding.ActivityRegisterBinding
import com.example.storyappapi2.utils.Resource
import com.example.storyappapi2.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.arahKeLogin.setOnClickListener{
            Intent(this@RegisterActivity, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.buttonRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if(name.isEmpty() && email.isEmpty() && password.isEmpty()){
                binding.edRegisterName.error = "Nama kosong"
                binding.edRegisterEmail.error = "Email kosong"
                binding.edRegisterPassword.error = "Password kosong"
            } else{
                registerViewModel.userRegister(name, email, password)
                registerViewModel.register.observe(this){
                    when(it){
                        is Resource.Success -> {
                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                this, it.message.toString(), Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}