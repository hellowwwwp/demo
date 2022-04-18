package com.example.demo.model.app

import androidx.recyclerview.widget.DiffUtil

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
data class AppItemEntity(
    val id: String,
    val rank: Int,
    val icon: String,
    val appName: String,
    val appTypeName: String,
    var rating: Float,
    var ratingCount: Int,
    var timestamp: Long,
) {
    companion object {
        val comparator = object : DiffUtil.ItemCallback<AppItemEntity>() {
            override fun areItemsTheSame(oldItem: AppItemEntity, newItem: AppItemEntity): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: AppItemEntity, newItem: AppItemEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}