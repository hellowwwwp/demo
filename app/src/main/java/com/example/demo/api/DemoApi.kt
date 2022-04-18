package com.example.demo.api

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.demo.DemoApplication
import com.example.demo.api.model.HttpResult
import com.example.demo.api.retrofit.SuspendCallAdapterFactory
import com.example.demo.viewmodel.model.app.AppListDataResult
import com.example.demo.viewmodel.model.app.AppRatingDataResult
import com.example.demo.viewmodel.model.app.AppRecommendListDataResult
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
interface DemoApi {

    @GET("hk/rss/topgrossingapplications/{limit}/json")
    suspend fun getAppRecommendListData(@Path("limit") limit: Int): HttpResult<AppRecommendListDataResult>

    @GET("hk/rss/topfreeapplications/{limit}/json")
    suspend fun getAppListData(@Path("limit") limit: Int): HttpResult<AppListDataResult>

    @GET("hk/lookup")
    suspend fun getAppRatingData(@Query("id") id: String): HttpResult<AppRatingDataResult>

    companion object {

        private const val BASE_URL = "https://itunes.apple.com/"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(SuspendCallAdapterFactory.create())
                .build()
        }

        private fun createChuckerInterceptor(): ChuckerInterceptor {
            return ChuckerInterceptor.Builder(DemoApplication.application)
                .maxContentLength(250000L)
                .alwaysReadResponseBody(false)
                .redactHeaders(emptySet())
                .build()
        }

        private fun createOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(createChuckerInterceptor())
                .build()
        }

        fun create(): DemoApi {
            return retrofit.create(DemoApi::class.java)
        }
    }

}