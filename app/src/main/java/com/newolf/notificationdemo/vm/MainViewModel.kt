package com.newolf.notificationdemo.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newolf.notificationdemo.R
import com.newolf.notificationdemo.data.NotificationBean
import com.newolf.notificationdemo.data.NotificationType

/**
 * ======================================================================
 *
 *
 * @author : NeWolf
 * @version : 1.0
 * @since :  2024-03-01
 *
 * =======================================================================
 */
class MainViewModel : ViewModel() {
    val notificationTypeList = MutableLiveData<ArrayList<NotificationBean>>()

    init {
//        composeData()
    }

    fun composeData() {
        val listData = ArrayList<NotificationBean>()
        listData.add(
            NotificationBean(
                NotificationType.BASIC_NOTIFICATION,
                R.string.basic_notification
            )
        )
        listData.add(
            NotificationBean(
                NotificationType.EXPANDABLE_NOTIFICATION,
                R.string.expandable_notification
            )
        )
        listData.add(
            NotificationBean(
                NotificationType.CALL_STYLE_NOTIFICATION,
                R.string.call_style_notification
            )
        )
        listData.add(
            NotificationBean(
                NotificationType.TIME_SENSITIVE_NOTIFICATION,
                R.string.time_sensitive_notification
            )
        )
        listData.add(
            NotificationBean(
                NotificationType.CUSTOM_NOTIFICATION,
                R.string.custom_notification
            )
        )
        notificationTypeList.value = listData
    }
}