package com.shinlee.network

sealed class ApiResult<out T> {
    val isSuccess get() = this is Success
    val isError get() = this is Error

    class Error(val throwable: Throwable) : ApiResult<Nothing>()
    class Success<out T>(val data: T) : ApiResult<T>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(throwable: Throwable) = Error(throwable)
    }

}