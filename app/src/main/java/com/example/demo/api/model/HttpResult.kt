package com.example.demo.api.model

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
sealed class HttpResult<out T> {
    val isSuccessful: Boolean
        get() = this is HttpSuccess<T>

    val isFailure: Boolean
        get() = this !is HttpSuccess<T>

    fun getOrNull(): T? {
        if (this is HttpSuccess<T>) {
            return value
        }
        return null
    }
}

data class HttpSuccess<T>(
    val value: T
) : HttpResult<T>()

data class HttpApiError(
    val code: Int,
    val msg: String
) : HttpResult<Nothing>()

data class HttpNetworkError(
    val throwable: Throwable
) : HttpResult<Nothing>()

data class HttpUnknownError(
    val throwable: Throwable
) : HttpResult<Nothing>()

/**
 * 判断是否是无网络的相关异常
 */
fun Throwable.isNonNetwork(): Boolean {
    return (this is UnknownHostException
            || this is SocketTimeoutException
            || this is ConnectException
            || this is SSLException)
}