package com.shinlee.network.handler

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import com.shinlee.network.Result

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            Result.success(body!!)
        } else {
            Result.error(HttpException(response))
        }
    } catch (e: IOException) {
        Result.error(e)
    } catch (e: HttpException) {
        Result.error(e)
    } catch (e: Exception) {
        Result.error(e)
    }
}