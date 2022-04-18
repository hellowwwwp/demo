package com.example.demo.viewmodel.model.app

import com.google.gson.annotations.SerializedName

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
data class AppRatingDataResult(
    @SerializedName("results")
    val results: List<AppRatingData>? = null
)