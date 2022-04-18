package com.example.demo.viewmodel.mapper

import com.example.demo.database.app.AppListItemBean
import com.example.demo.database.app.AppRecommendListItemBean
import com.example.demo.model.app.AppItemEntity
import com.example.demo.viewmodel.model.app.AppItemData

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
object AppDataMapper {

    fun appItemData2AppItemEntity(dataList: List<AppItemData>?): List<AppItemEntity>? {
        if (dataList.isNullOrEmpty()) {
            return null
        }
        val result = mutableListOf<AppItemEntity>()
        dataList.forEachIndexed { index, appItemData ->
            val entity = appItemData2AppItemEntity(appItemData, index)
            result.add(entity)
        }
        return result
    }

    private fun appItemData2AppItemEntity(data: AppItemData, index: Int): AppItemEntity {
        return AppItemEntity(
            data.id?.attributes?.id ?: "",
            index,
            data.image?.lastOrNull()?.label ?: "",
            data.title?.label ?: "",
            data.category?.attributes?.label ?: "",
            data.averageUserRating,
            data.userRatingCount,
            data.timestamp
        )
    }

    fun appItemData2AppListItemBean(dataList: List<AppItemData>?): List<AppListItemBean>? {
        if (dataList.isNullOrEmpty()) {
            return null
        }
        val result = mutableListOf<AppListItemBean>()
        dataList.forEachIndexed { index, appItemData ->
            val bean = appItemData2AppListItemBean(appItemData, index)
            result.add(bean)
        }
        return result
    }

    private fun appItemData2AppListItemBean(data: AppItemData, index: Int): AppListItemBean {
        return AppListItemBean(
            data.id?.attributes?.id ?: "",
            index,
            data.image?.lastOrNull()?.label ?: "",
            data.title?.label ?: "",
            data.category?.attributes?.label ?: "",
            data.averageUserRating,
            data.userRatingCount
        )
    }

    fun appItemData2AppRecommendListItemBean(dataList: List<AppItemData>?): List<AppRecommendListItemBean>? {
        if (dataList.isNullOrEmpty()) {
            return null
        }
        val result = mutableListOf<AppRecommendListItemBean>()
        dataList.forEachIndexed { index, appItemData ->
            val bean = appItemData2AppRecommendListItemBean(appItemData, index)
            result.add(bean)
        }
        return result
    }

    private fun appItemData2AppRecommendListItemBean(data: AppItemData, index: Int): AppRecommendListItemBean {
        return AppRecommendListItemBean(
            data.id?.attributes?.id ?: "",
            index,
            data.image?.lastOrNull()?.label ?: "",
            data.title?.label ?: "",
            data.category?.attributes?.label ?: "",
            data.averageUserRating,
            data.userRatingCount
        )
    }

    fun appListItemBean2AppItemEntity(beanList: List<AppListItemBean>?): List<AppItemEntity>? {
        if (beanList.isNullOrEmpty()) {
            return null
        }
        val result = mutableListOf<AppItemEntity>()
        beanList.forEachIndexed { index, appItemData ->
            val entity = appListItemBean2AppItemEntity(appItemData)
            result.add(entity)
        }
        return result
    }

    private fun appListItemBean2AppItemEntity(bean: AppListItemBean): AppItemEntity {
        return AppItemEntity(
            bean.id,
            bean.rank,
            bean.icon,
            bean.appName,
            bean.appType,
            bean.rating,
            bean.ratingCount,
            System.currentTimeMillis()
        )
    }

    fun appRecommendListItemBean2AppItemEntity(beanList: List<AppRecommendListItemBean>?): List<AppItemEntity>? {
        if (beanList.isNullOrEmpty()) {
            return null
        }
        val result = mutableListOf<AppItemEntity>()
        beanList.forEachIndexed { index, appItemData ->
            val entity = appRecommendListItemBean2AppItemEntity(appItemData)
            result.add(entity)
        }
        return result
    }

    private fun appRecommendListItemBean2AppItemEntity(bean: AppRecommendListItemBean): AppItemEntity {
        return AppItemEntity(
            bean.id,
            bean.rank,
            bean.icon,
            bean.appName,
            bean.appType,
            bean.rating,
            bean.ratingCount,
            System.currentTimeMillis()
        )
    }

}