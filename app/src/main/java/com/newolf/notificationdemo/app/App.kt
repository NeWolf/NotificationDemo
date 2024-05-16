package com.newolf.notificationdemo.app

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * ======================================================================
 *
 *
 * @author : NeWolf
 * @version : 1.0
 * @since :  2024-02-29
 *
 * =======================================================================
 */
class App : Application() {
    companion object {
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        app = this
    }
}