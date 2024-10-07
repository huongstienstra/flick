package com.shinlee.network.handler

import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import com.shinlee.network.ApiResult
import com.shinlee.network.model.BaseResponse

class ApiError(message: String) : Exception(message)

suspend fun <T : BaseResponse> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                if (body.error != null) {
                    // API returned an error message
                    ApiResult.error(ApiError(body.error))
                } else {
                    ApiResult.success(body)
                }
            } else {
                ApiResult.error(ApiError("Response body is null"))
            }
        } else {
            // Try to parse error body
            val errorBody = response.errorBody()?.string()
            val errorResponse = try {
                Gson().fromJson(errorBody, BaseResponse::class.java)
            } catch (e: Exception) {
                null
            }
            ApiResult.error(ApiError(errorResponse?.error ?: "Unknown error occurred"))
        }
    } catch (e: IOException) {
        ApiResult.error(e)
    } catch (e: HttpException) {
        ApiResult.error(e)
    } catch (e: Exception) {
        ApiResult.error(e)
    }
}