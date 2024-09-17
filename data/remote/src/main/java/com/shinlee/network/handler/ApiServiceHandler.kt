package com.shinlee.network.handler

import android.util.Log
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import com.shinlee.network.Result
import com.shinlee.network.model.BaseResponse
import com.shinlee.network.model.ErrorData

suspend fun <T: BaseResponse> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            Result.success(body!!)

            //fixme: handle APi flags if needed
//            if (body != null) {
//                val flag = body.code
//                if (flag == 200) {
//                    Log.e("API", "body ${flag}")
//                    Result.success(body)
//                } else {
//                    Log.e("API", "error ${flag}")
//                    Result.error(Throwable(message = body.message))
//                }
//            } else Result.error(NullPointerException("Response body is null"))
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