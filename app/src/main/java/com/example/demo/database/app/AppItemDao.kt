package com.example.demo.database.app

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
@Dao
interface AppItemDao {

    @Query("select * from app_list_item_bean order by rank asc")
    fun getAllAppListItems(): List<AppListItemBean>?

    @Query("delete from app_list_item_bean")
    fun deleteAllAppListItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAppListItems(items: List<AppListItemBean>)

    @Query("select * from app_recommend_list_item_bean order by rank asc")
    fun getAllAppRecommendListItems(): List<AppRecommendListItemBean>?

    @Query("delete from app_recommend_list_item_bean")
    fun deleteAllAppRecommendListItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAppRecommendListItems(items: List<AppRecommendListItemBean>)

}