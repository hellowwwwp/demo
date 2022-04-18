package com.automizely.simpleview.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.demo.database.app.AppItemDao
import com.example.demo.database.app.AppListItemBean
import com.example.demo.database.app.AppRecommendListItemBean

/**
 * @author: wangpan
 * @email: p.wang@aftership.com
 * @date: 2021/10/27
 */
@Database(entities = [AppListItemBean::class, AppRecommendListItemBean::class], version = 1, exportSchema = true)
abstract class DemoRoomDatabase : RoomDatabase() {

    abstract fun getAppItemDao(): AppItemDao

    companion object {

        @Volatile
        private var instance: DemoRoomDatabase? = null

        fun get(context: Context): DemoRoomDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    DemoRoomDatabase::class.java,
                    "demo_database"
                ).build().also {
                    instance = it
                }
            }
        }

    }

}