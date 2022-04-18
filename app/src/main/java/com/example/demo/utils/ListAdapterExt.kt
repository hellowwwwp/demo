package com.example.demo.utils

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
fun <T> DiffUtil.ItemCallback<T>.asAsyncDifferConfig(): AsyncDifferConfig<T> {
    return AsyncDifferConfig.Builder(this)
        .setBackgroundThreadExecutor(Dispatchers.Default.asExecutor())
        .build()
}

suspend fun <T, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.submitListAwaitCommitted(list: List<T>?) {
    return suspendCoroutine { cont ->
        submitList(list) {
            cont.resume(Unit)
        }
    }
}