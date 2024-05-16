package com.newolf.notificationdemo.data

import androidx.annotation.StringRes

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
data class NotificationBean(@NotificationType val type: Int, @StringRes val showItem: Int)
