package com.example.demo.model.search

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.DiffUtil

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
data class LocalAppEntity(
    val packageName: String,
    val appName: String,
    val icon: Drawable,
) {
    companion object {
        val comparator = object : DiffUtil.ItemCallback<LocalAppEntity>() {
            override fun areItemsTheSame(oldItem: LocalAppEntity, newItem: LocalAppEntity): Boolean {
                return oldItem.packageName == newItem.packageName
            }

            override fun areContentsTheSame(oldItem: LocalAppEntity, newItem: LocalAppEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}