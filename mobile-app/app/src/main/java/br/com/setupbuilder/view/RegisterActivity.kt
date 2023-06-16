package br.com.setupbuilder.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import br.com.setupbuilder.R
import br.com.setupbuilder.controller.UserController
import kotlinx.android.synthetic.main.register_activity.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        button_register.setOnClickListener {
            progressBarRegister.visibility = View.VISIBLE
            createUser()
        }

        textLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUser() {
        val email = email_register.text.toString()
        val password = password_register.text.toString()
        var valid = true
        val repository = UserController()

        if (email.isEmpty() || password.isEmpty()) {
            Patterns.EMAIL_ADDRESS

            if (email.isEmpty())
                email_register.setError("Kolom ini tidak boleh kosong!")
            if (password.isEmpty())
                password_register.setError("Kolom ini tidak boleh kosong!")

            valid = false
        }

        if (!password.equals(password_register2.text.toString())) {
            password_register2.setError("Santi tidak sesuai")
            valid = false
        }

        if (password.length < 6) {
            password_register.setError("Kata sandi Anda harus berisi setidaknya 6 karakter")
            valid = false
        }


        if (!valid) {
            progressBarRegister.visibility = View.INVISIBLE
            return
        }

        repository.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    progressBarRegister.visibility = View.INVISIBLE
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                }
            }.addOnFailureListener {
                progressBarRegister.visibility = View.INVISIBLE
                if (it.message.toString().contains("already in use")) {
                    email_register.setError("E-mail ini sudah digunakan")
                } else
                    if (it.message.toString().contains("badly formatted")) {
                        email_register.setError("Masukkan e-mail yang valid")
                    } else
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }
}