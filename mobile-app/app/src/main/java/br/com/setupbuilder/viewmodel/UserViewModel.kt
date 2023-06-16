package br.com.setupbuilder.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import br.com.setupbuilder.controller.UserController
import br.com.setupbuilder.view.MenuActivity

class UserViewModel {
    private var mUser = UserController()
    public fun SignIn(email:EditText, password:EditText, context:Context):Boolean{

        var result = false
        mUser.signIn(email.text.toString(), password.text.toString())
            .addOnSuccessListener {
                result = true
                val intent = Intent(context, MenuActivity::class.java)
                context.startActivity(intent)
                return@addOnSuccessListener

            }
            .addOnFailureListener {
                if(it.message.toString().contains("password is invalid")){
                    password.setError("Kata sandi salah")
                }else
                    if(it.message.toString().contains("There is no user")){
                        email.setError("Email tidak terdaftar.")
                    }else
                        if(it.message.toString().contains("badly formatted")) {
                            email.setError("Email tidak valid.")
                        }else{
                            Toast.makeText(
                                context,
                                "Kesalahan tak normal telah terjadi. Harap coba lagi.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                return@addOnFailureListener

            }
        return result
    }
}