package com.programmer270487.loginsignuptypicode.data.repo

import android.content.Context
import com.programmer270487.loginsignuptypicode.utils.State

interface AuthRepo {
    fun registerUser(
        uname: String,
        email: String,
        password: String,
        role: Boolean,
        result: (State<String>) -> Unit
    )
    fun loginUser(
        email: String,
        password: String,
        result: (State<String>) -> Unit
    )
    fun logout(context: Context, result: () -> Unit)
    fun handleOnStart(context: Context, result: (Boolean, Boolean) -> Unit)
}