package com.example.demo.utils

import android.os.Looper
import androidx.annotation.AnyThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
val isMainThread: Boolean
    get() = Looper.getMainLooper() == Looper.myLooper()

@AnyThread
inline fun <reified T> MutableLiveData<T>.setOrPostValue(map: (T) -> T) {
    if (isMainThread) {
        value = map(verifyLiveDataNotEmpty())
    } else {
        postValue(map(verifyLiveDataNotEmpty()))
    }
}

inline fun <reified T> LiveData<T>.verifyLiveDataNotEmpty(): T {
    return value ?: throw NullPointerException("LiveData<${T::class.java}> not contain value.")
}

inline fun <reified T> MutableLiveData<T>.requireValue(): T {
    verifyLiveDataNotEmpty()
    return value!!
}