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
 * @since :  2024-03-07
 *
 * =======================================================================
 */
class BasicNotificationViewModel : ViewModel() {
    val notificationTypeList = MutableLiveData<ArrayList<NotificationBean>>()

    init {
//        composeData()
    }

    fun composeData() {
        val listData = ArrayList<NotificationBean>().apply {
            add(
                NotificationBean(
                    NotificationType.BASIC_ADD_ACTION_BUTTONS,
                    R.string.basic_add_action_buttons
                )
            )
            add(
                NotificationBean(
                    NotificationType.BASIC_DIRECT_REPLY,
                    R.string.basic_direct_reply_msg
                )
            )
            add(
                NotificationBean(
                    NotificationType.BASIC_PROGRESS_BAR_A,
                    R.string.basic_progress_bar_a
                )
            )
            add(
                NotificationBean(
                    NotificationType.BASIC_PROGRESS_BAR_B,
                    R.string.basic_progress_bar_b
                )
            )
            add(
                NotificationBean(
                    NotificationType.BASIC_SYSTEM_WIDE_CATEGORY,
                    R.string.basic_add_system_wide_category
                )
            )


            add(
                NotificationBean(
                    NotificationType.BASIC_URGENT_MESSAGE,
                    R.string.basic_urgent_msg
                )
            )
            add(
                NotificationBean(
                    NotificationType.BASIC_LOCK_SCREEN_VISIBILITY,
                    R.string.basic_lock_screen_visibility
                )
            )
            add(
                NotificationBean(
                    NotificationType.BASIC_UPDATE,
                    R.string.basic_update
                )
            )
            add(
                NotificationBean(
                    NotificationType.BASIC_REMOVE,
                    R.string.basic_remove
                )
            )
        }
        notificationTypeList.value = listData
    }

}