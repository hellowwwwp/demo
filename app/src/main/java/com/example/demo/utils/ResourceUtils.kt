package com.example.demo.utils

import android.content.Context
import com.example.demo.DemoApplication

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
object ResourceUtils {

    fun getString(context: Context = DemoApplication.application, resId: Int): String {
        return context.getString(resId)
    }

}