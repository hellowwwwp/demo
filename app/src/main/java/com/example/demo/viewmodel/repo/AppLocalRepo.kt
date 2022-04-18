package com.example.demo.viewmodel.repo

import com.automizely.simpleview.room.DemoRoomDatabase
import com.example.demo.DemoApplication
import com.example.demo.database.app.AppItemDao
import com.example.demo.database.app.AppListItemBean
import com.example.demo.database.app.AppRecommendListItemBean
import com.example.demo.viewmodel.mapper.AppDataMapper
import com.example.demo.viewmodel.model.app.AppItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class AppLocalRepo {

    private val appItemDao: AppItemDao by lazy {
        DemoRoomDatabase.get(DemoApplication.application).getAppItemDao()
    }

    suspend fun getAllAppListItems(): List<AppListItemBean>? {
        return withContext(Dispatchers.IO) {
            appItemDao.getAllAppListItems()
        }
    }

    suspend fun deleteAllAppListItems() = supervisorScope {
        withContext(Dispatchers.IO) {
            appItemDao.deleteAllAppListItems()
        }
    }

    suspend fun insertAppListItems(items: List<AppItemData>?) = supervisorScope {
        withContext(Dispatchers.IO) {
            val beanList = AppDataMapper.appItemData2AppListItemBean(items)
            if (!beanList.isNullOrEmpty()) {
                appItemDao.insertAppListItems(beanList)
            }
        }
    }

    suspend fun getAllAppRecommendListItems(): List<AppRecommendListItemBean>? {
        return withContext(Dispatchers.IO) {
            appItemDao.getAllAppRecommendListItems()
        }
    }

    suspend fun deleteAllAppRecommendListItems() = supervisorScope {
        withContext(Dispatchers.IO) {
            appItemDao.deleteAllAppRecommendListItems()
        }
    }

    suspend fun insertAppRecommendListItems(items: List<AppItemData>?) = supervisorScope {
        withContext(Dispatchers.IO) {
            val beanList = AppDataMapper.appItemData2AppRecommendListItemBean(items)
            if (!beanList.isNullOrEmpty()) {
                appItemDao.insertAppRecommendListItems(beanList)
            }
        }
    }

}