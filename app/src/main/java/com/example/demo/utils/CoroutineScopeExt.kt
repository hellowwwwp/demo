package com.example.demo.utils

import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/9
 */
fun CoroutineScope.launchByCatch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    exceptionHandler: ((CoroutineContext, Throwable) -> Unit) = { _, throwable ->
        throwable.printStackTrace()
        Log.e("tag", "launchByCatch throwable: $throwable")
    },
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(context + CoroutineExceptionHandler(exceptionHandler), start, block)
}

suspend fun <T> withContextOrNull(
    context: CoroutineContext,
    errorBlock: ((Throwable) -> Unit) = { throwable ->
        throwable.printStackTrace()
        Log.e("tag", "withContextOrNull throwable: $throwable")
    },
    block: suspend CoroutineScope.() -> T
): T? {
    return try {
        withContext(context, block)
    } catch (t: Throwable) {
        errorBlock(t)
        null
    }
}

suspend fun <T> withContextByCatch(
    context: CoroutineContext,
    errorBlock: suspend CoroutineScope.(Throwable) -> T,
    block: suspend CoroutineScope.() -> T
): T {
    return try {
        withContext(context, block)
    } catch (t: Throwable) {
        coroutineScope {
            errorBlock(t)
        }
    }
}