package com.example.demo.viewmodel.model.app

import com.google.gson.annotations.SerializedName

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
data class AppItemData(
    @SerializedName("im:name")
    val name: AppName? = null,
    @SerializedName("im:image")
    val image: List<AppImage>? = null,
    @SerializedName("summary")
    val summary: AppSummary? = null,
    @SerializedName("im:contentType")
    val contentType: AppContentType? = null,
    @SerializedName("title")
    val title: AppTitle? = null,
    @SerializedName("id")
    val id: AppId? = null,
    @SerializedName("im:artist")
    val artist: AppArtist? = null,
    @SerializedName("category")
    val category: AppCategory? = null,
    //应用星级
    var averageUserRating: Float = 0f,
    //评论数
    var userRatingCount: Int = 0,
    //TODO 用来区分 AppItemData 用, 因为每次调用接口返回的都是一样的数据
    var timestamp: Long = Long.MIN_VALUE,
)

data class AppName(
    @SerializedName("label")
    val label: String? = null
)

data class AppImage(
    @SerializedName("label")
    val label: String? = null,
    @SerializedName("attributes")
    val attributes: AppImageAttributes? = null
)

data class AppImageAttributes(
    @SerializedName("height")
    val height: String? = null
)

data class AppSummary(
    @SerializedName("label")
    val label: String? = null
)

data class AppContentType(
    @SerializedName("attributes")
    val attributes: AppContentTypeAttributes? = null
)

data class AppContentTypeAttributes(
    @SerializedName("term")
    val term: String? = null,
    @SerializedName("label")
    val label: String? = null
)

data class AppTitle(
    @SerializedName("label")
    val label: String? = null
)

data class AppId(
    @SerializedName("label")
    val label: String? = null,
    @SerializedName("attributes")
    val attributes: AppIdAttributes? = null
)

data class AppIdAttributes(
    @SerializedName("im:id")
    val id: String? = null,
    @SerializedName("im:bundleId")
    val bundleId: String? = null
)

data class AppArtist(
    @SerializedName("label")
    val label: String? = null,
    @SerializedName("attributes")
    val attributes: AppArtistAttributes? = null
)

data class AppArtistAttributes(
    @SerializedName("href")
    val href: String? = null
)

data class AppCategory(
    @SerializedName("attributes")
    val attributes: AppCategoryAttributes? = null
)

data class AppCategoryAttributes(
    @SerializedName("im:id")
    val id: String? = null,
    @SerializedName("term")
    val term: String? = null,
    @SerializedName("scheme")
    val scheme: String? = null,
    @SerializedName("label")
    val label: String? = null
)

data class AppRatingData(
    @SerializedName("averageUserRating")
    val averageUserRating: Float = 0f,
    @SerializedName("userRatingCount")
    val userRatingCount: Int = 0,
)

data class FeedData(
    @SerializedName("entry")
    val entry: List<AppItemData>? = null
)