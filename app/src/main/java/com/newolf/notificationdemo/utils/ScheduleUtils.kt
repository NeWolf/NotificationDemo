package com.newolf.notificationdemo.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.newolf.notificationdemo.receiver.NotificationReceiver
import java.util.concurrent.TimeUnit

/**
 * ======================================================================
 *
 *
 * @author : NeWolf
 * @version : 1.0
 * @since :  2024-03-08
 *
 * =======================================================================
 */

private const val SCHEDULE_TIME = 5L

@SuppressLint("ScheduleExactAlarm")
fun Context.scheduleNotification(isLockScreen: Boolean) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val timeInMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(SCHEDULE_TIME)

    with(alarmManager) {
        setExact(AlarmManager.RTC_WAKEUP, timeInMillis, getReceiver(isLockScreen))
    }
}

fun Context.getReceiver(isLockScreen: Boolean): PendingIntent {
    return PendingIntent.getBroadcast(
        this,
        System.currentTimeMillis().hashCode(),
        NotificationReceiver.build(this, isLockScreen),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}
