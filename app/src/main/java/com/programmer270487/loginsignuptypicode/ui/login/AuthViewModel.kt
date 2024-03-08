package com.programmer270487.loginsignuptypicode.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.programmer270487.loginsignuptypicode.data.repo.AuthRepo
import com.programmer270487.loginsignuptypicode.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(val authRepo: AuthRepo): ViewModel() {
    private val _register = MutableLiveData<State<String>>()
    val register: LiveData<State<String>>
        get() = _register

    private val _login = MutableLiveData<State<String>>()
    val login: LiveData<State<String>>
        get() = _login

    fun register(
        uname: String,
        email: String,
        password: String,
        role: Boolean
    ) {
        _register.value = State.Loading
        authRepo.registerUser(uname, email, password, role) {
            _register.value = it
        }
    }
    fun login(
        email: String,
        password: String,
    ) {
        _login.value = State.Loading
        authRepo.loginUser(email, password) {
            _login.value = it
        }
    }

    fun logOut(context: Context, result: () -> Unit) {
        authRepo.logout(context, result)
    }

    fun handleOnStart(context: Context, result: (Boolean, Boolean) -> Unit) {
        authRepo.handleOnStart(context, result)
    }
}