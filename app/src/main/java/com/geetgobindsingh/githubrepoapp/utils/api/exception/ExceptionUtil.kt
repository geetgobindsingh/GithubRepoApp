package com.geetgobindingh.githubrepoapp.util.exception

import com.geetgobindingh.githubrepoapp.util.api.exception.RetrofitException

object ExceptionUtil {
    private val NETWORK_ERROR = "Network Error"

    fun getErrorText(throwable: Throwable?): String {
        var result = ""
        if (throwable == null) {
            return NETWORK_ERROR
        }

        try {
            if (throwable is RetrofitException) {
                val retrofitException = throwable as? RetrofitException?
                val apiError = retrofitException?.getErrorData()
                result = apiError?.message ?: "Api Error"
            } else if (throwable.message != null) {
                result = if (throwable.message != null) throwable.message.toString() else ""
            } else {
                result = NETWORK_ERROR
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return NETWORK_ERROR
        }

        return result
    }
}
