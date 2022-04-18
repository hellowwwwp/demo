package com.example.demo.database.app

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
@Entity(tableName = "app_list_item_bean")
data class AppListItemBean(
    @PrimaryKey
    val id: String,
    val rank: Int,
    val icon: String,
    val appName: String,
    val appType: String,
    val rating: Float,
    val ratingCount: Int
)