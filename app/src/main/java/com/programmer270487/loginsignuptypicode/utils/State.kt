package com.programmer270487.loginsignuptypicode.utils

sealed class State<out T> {
    data object Loading: State<Nothing>() // object Loading: State<Nothing>()
    data class Success<out T>(val data: T): State<T>()
    data class Failure(val error: String?): State<Nothing>()
}