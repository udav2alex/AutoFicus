package ru.gressor.autoficus.data.model

sealed class RequestResult {
    data class Success<out T>(val data: T): RequestResult()
    data class Error(val error: Throwable): RequestResult()
}