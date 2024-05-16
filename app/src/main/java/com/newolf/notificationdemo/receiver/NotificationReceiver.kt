package com.newolf.notificationdemo.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.LogUtils
import com.newolf.notificationdemo.FullScreenActivity
import com.newolf.notificationdemo.LockScreenActivity
import com.newolf.notificationdemo.R
import com.newolf.notificationdemo.utils.NotificationHelper

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val isLockScreen = intent.getBooleanExtra(LOCK_SCREEN_KEY, false)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val destination = if (isLockScreen) {
                LockScreenActivity::class.java
            } else {
                FullScreenActivity::class.java
            }
            val activityIntent = Intent(context, destination).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(activityIntent)
            LogUtils.e("SDK_INT = ${Build.VERSION.SDK_INT} is bellow Q,direct startActivity isLockScreen = $isLockScreen")
            return
        }
        if (isLockScreen) {
            showNotificationWithFullScreenIntent(context, true)
        } else {

            showNotificationWithFullScreenIntent(context)
        }
    }

    private fun showNotificationWithFullScreenIntent(
        context: Context,
        isLockScreen: Boolean = false,
    ) {
        val basicNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, NotificationHelper.getMainNotificationId())
        } else {
            NotificationCompat.Builder(context)
        }
        basicNotification.setSmallIcon(R.drawable.tj809)
            .setContentTitle("Waring!!!")
            .setContentText("Plan is start")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(getPendingIntent(context, isLockScreen), true)
            .setAutoCancel(true)


        NotificationHelper.sendNotification(basicNotification.build())
        LogUtils.e("sendFullScreenIntentNotification isLockScreen = $isLockScreen")

    }

    private fun getPendingIntent(context: Context, isLockScreen: Boolean): PendingIntent {
        val destination = if (isLockScreen) {
            LockScreenActivity::class.java
        } else {
            FullScreenActivity::class.java
        }

        return PendingIntent.getActivity(
            context,
            System.currentTimeMillis().hashCode(),
            Intent(context, destination),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    }

    companion object {
        const val LOCK_SCREEN_KEY = "LOCK_SCREEN_KEY"
        fun build(context: Context, isLockScreen: Boolean): Intent {
            return Intent(context, NotificationReceiver::class.java).also {
                it.putExtra(LOCK_SCREEN_KEY, isLockScreen)
            }
        }
    }
}