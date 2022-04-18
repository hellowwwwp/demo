package com.example.demo.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBar()
    }

    protected open fun initBar() {
        ImmersionBar.with(this).apply {
            statusBarColor(android.R.color.white)
            navigationBarColor(android.R.color.white)
            statusBarDarkFont(true)
            navigationBarDarkIcon(true)
            fitsSystemWindows(true)
        }.init()
    }

}