package com.dicoding.kreatoroom.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kreatoroom.ui.data.RegisterResponse
import com.example.storyappapi2.api.ApiClient
import com.example.storyappapi2.utils.Resource
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private val _register = MutableLiveData<Resource<RegisterResponse>>()
    val register: LiveData<Resource<RegisterResponse>> = _register

    fun userRegister(name: String, email: String, password: String){
        val client = ApiClient.apiInstance.userRegister(name,email,password)
        Log.d("TAG", "userLogin: $client")
        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if(response.isSuccessful){
                    _register.value = response.body()?.let{ Resource.Success(it) }
                    Log.d("RegisterViewModel", "onResponse: ${_register.value}")
                }
                else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let{
                        JSONObject(it).getString("message")
                    }
                    _register.value = Resource.Error(errorMessage)
                    Log.e("RegisterViewModel", "onResponse: $errorMessage")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _register.value = Resource.Error("${t.message}")
                Log.e("RegisterViewModel", "onFailure: ${t.message}")
            }
        })
    }
}