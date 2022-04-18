package com.example.demo.viewmodel.model.app

import android.os.SystemClock
import com.google.gson.annotations.SerializedName

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
data class AppListDataResult(
    @SerializedName("feed")
    val feed: FeedData? = null
) {
    val items: List<AppItemData>?
        get() {
            //TODO 给每一个 AppItemData 的 timestamp 赋值
            val now = SystemClock.uptimeMillis()
            feed?.entry?.forEach {
                it.timestamp = now
            }
            return feed?.entry
        }
}