package com.example.demo.viewmodel.repo

import com.example.demo.api.DemoApi
import com.example.demo.api.model.HttpResult
import com.example.demo.viewmodel.model.app.AppItemData
import com.example.demo.viewmodel.model.app.AppListDataResult
import com.example.demo.viewmodel.model.app.AppRecommendListDataResult
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class AppRemoteRepo {

    private val demoApi: DemoApi by lazy {
        DemoApi.create()
    }

    suspend fun getAppRecommendListData(limit: Int): HttpResult<AppRecommendListDataResult> {
        return demoApi.getAppRecommendListData(limit)
    }

    suspend fun getAppListData(limit: Int): HttpResult<AppListDataResult> {
        return demoApi.getAppListData(limit)
    }

    suspend fun queryRatingDetail(appDataList: List<AppItemData>?) = supervisorScope {
        if (appDataList.isNullOrEmpty()) {
            return@supervisorScope
        }
        //同时批量查询评分信息
        appDataList.map { item ->
            val id = item.id?.attributes?.id ?: ""
            async {
                val appRatingDataResult = demoApi.getAppRatingData(id)
                val appRatingData = appRatingDataResult.getOrNull()?.results?.firstOrNull()
                if (appRatingData != null) {
                    item.averageUserRating = appRatingData.averageUserRating
                    item.userRatingCount = appRatingData.userRatingCount
                }
            }
        }.forEach {
            it.await()
        }
    }

}