package com.example.demo.api.retrofit

import com.example.demo.api.model.*
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class SuspendCallAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        //suspend 函数的返回值是 Call 类型(被 Retrofit 动态修改了)
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        //检查 Call 是否含有泛型, 默认一定会有泛型
        check(returnType is ParameterizedType)

        //获取 Call 里面的泛型
        val httpResultType = getParameterUpperBound(0, returnType)

        //如果 Call 的泛型不是 HttpResult<Foo> 则不用我们处理
        if (getRawType(httpResultType) != HttpResult::class.java) {
            return null
        }

        //检查 HttpResult<Foo> 是否含有泛型, 默认一定会有泛型
        check(httpResultType is ParameterizedType)

        //获取 HttpResult<Foo> 里面的泛型
        val rawType = getParameterUpperBound(0, httpResultType)

        return SuspendCallAdapter<Any>(rawType)
    }

    companion object {
        fun create(): SuspendCallAdapterFactory {
            return SuspendCallAdapterFactory()
        }
    }
}

internal class SuspendCallAdapter<R>(
    private val responseType: Type
) : CallAdapter<R, Call<HttpResult<R>>> {
    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): Call<HttpResult<R>> {
        return SuspendCall(call)
    }
}

/**
 * 将 Foo 类型转换为 HttpResult<Foo> 类型返回
 */
internal class SuspendCall<R>(
    private val delegate: Call<R>
) : Call<HttpResult<R>> {

    /**
     * 这个方法会由 KotlinExtensions.kt 里面来调用, Retrofit 在 2.6.0 就支持了声明挂起函数作为接口,
     * 在业务方调用挂起函数接口时, 会从挂起函数的参数中拿到 continuation 对象调用 await 方法
     */
    override fun enqueue(callback: Callback<HttpResult<R>>) {
        delegate.enqueue(object : Callback<R> {
            override fun onResponse(call: Call<R>, response: Response<R>) {
                val httpResult = if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        HttpSuccess(body)
                    } else {
                        HttpUnknownError(NullPointerException("body is null"))
                    }
                } else {
                    val code = response.code()
                    val msg = response.message() ?: "api error"
                    HttpApiError(code, msg)
                }
                callback.onResponse(this@SuspendCall, Response.success(httpResult))
            }

            override fun onFailure(call: Call<R>, t: Throwable) {
                val httpResult = if (t.isNonNetwork()) {
                    HttpNetworkError(t)
                } else {
                    HttpUnknownError(t)
                }
                callback.onResponse(this@SuspendCall, Response.success(httpResult))
            }
        })
    }

    override fun execute(): Response<HttpResult<R>> {
        throw UnsupportedOperationException("SuspendCall not support execute")
    }

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun cancel() {
        return delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun clone(): Call<HttpResult<R>> {
        return SuspendCall(delegate)
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }
}