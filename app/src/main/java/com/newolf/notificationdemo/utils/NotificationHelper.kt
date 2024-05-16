package com.newolf.notificationdemo.utils

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import com.newolf.notificationdemo.R
import com.newolf.notificationdemo.app.App


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
object NotificationHelper {
    const val TAG = "NotificationHelper"

    private val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(
            App.app
        )
    }


    private val appOpsManager: AppOpsManager by lazy { App.app.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager }


    private fun areNotificationsEnabled(): Boolean {
        var isEnable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            isEnable = notificationManager.areNotificationsEnabled()
        }
        LogUtils.dTag(TAG, "areNotificationsEnabled = $isEnable")
        return isEnable
    }

    private fun isNotificationsEnabled(): Boolean {
        val appInfo = App.app.applicationInfo
        val pkg = appInfo.packageName
        val uid = appInfo.uid
        var isEnable = true
        try {
            val clazz: Class<*> = Class.forName(AppOpsManager::class.java.name)
            val method = clazz.getDeclaredMethod(
                "checkOpNoThrow",
                Int::class.java,
                Int::class.java,
                String::class.java
            )
            val opPostNotificationField = clazz.getDeclaredField("OP_POST_NOTIFICATION")
            val opPostNotificationValue = opPostNotificationField.get(Int::class.java)
            LogUtils.e("isNotificationsEnabled:: opPostNotificationValue = $opPostNotificationValue")
            val fieldValue = opPostNotificationValue as Int

            isEnable = method.invoke(
                appOpsManager,
                fieldValue,
                uid,
                pkg
            ) as Int == AppOpsManager.MODE_ALLOWED

            LogUtils.e("isNotificationsEnabled = $isEnable")

        } catch (e: Exception) {
            LogUtils.e(e)
        }

        return isEnable
    }

    fun checkNotificationsEnabled(): Boolean {
        val result = areNotificationsEnabled() and isNotificationsEnabled() and isChannelEnabled()
        LogUtils.dTag(TAG, "checkNotificationsEnabled result = $result")
        return result
    }

    fun isChannelEnabled(): Boolean {
        var isEnable = true

        val channel =
            notificationManager.getNotificationChannel(App.app.getString(R.string.main_notification_id))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LogUtils.e("channel = $channel ,importance =  ${channel?.importance} ")
            val importance = channel?.importance
            if (importance != null) {
                isEnable = importance >= NotificationManager.IMPORTANCE_LOW
            }
        }
        LogUtils.dTag(TAG, "isChannelEnabled isEnable = $isEnable")
        return isEnable
    }

    fun requestNotificationsPermission(
        activity: ComponentActivity,
        customRequestPermissionDialog: Dialog? = null
    ): Boolean {
        var hasPermission = false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }

        val requestPermissionLauncher: ActivityResultLauncher<String> =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                Log.wtf(TAG, "registerForActivityResult = $isGranted ")
                if (isGranted) {
                    hasPermission = true
                } else {
                    if (customRequestPermissionDialog?.isShowing == false) {
                        customRequestPermissionDialog.show()
                    }
                }
            }

        hasPermission = if (ContextCompat.checkSelfPermission(
                App.app,
                Manifest.permission.POST_NOTIFICATIONS
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            false
        }

        return hasPermission
    }


    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

    }

    fun getMainNotificationId(): String {
        return App.app.resources.getString(R.string.main_notification_id)
    }

    fun getMainNotificationDescription(): String {
        return App.app.resources.getString(R.string.main_notification_description)
    }

    fun getMainNotificationName(): String {
        return App.app.resources.getString(R.string.main_notification_name)
    }

    fun registerMainNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                getMainNotificationId(),
                getMainNotificationName(),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.description = getMainNotificationDescription()
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private var notificationId = 0

    fun getSendNotificationId(): Int {
        return notificationId
    }

    fun updateNotificationId(): NotificationHelper {
        notificationId += 1
        return this
    }

    private fun updateAndGetNotificationId(): Int {
        notificationId += 1
        return notificationId
    }

    fun sendNotification(
        notification: Notification,
        notificationId: Int = updateAndGetNotificationId()
    ) {
        if (ActivityCompat.checkSelfPermission(
                App.app,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(notificationId, notification)
    }

    fun cancelAll() {
        notificationManager.cancelAll()
    }

    fun toNotifySetting(context: Context) {
        val intent = Intent()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //适配 8.0及8.0以上(8.0需要先打开应用通知，再打开渠道通知)
                if (isChannelEnabled()) {
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                } else {
                    intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, getMainNotificationId())
                }
            } else { // 适配 5.0及5.0以上
                intent.putExtra("app_package", context.packageName)
                intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
                intent.putExtra("app_uid", context.applicationInfo.uid)
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS")

            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            LogUtils.eTag(TAG, "toNotifySetting:$intent")
            context.startActivity(intent)
        } catch (e: Exception) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.setData(uri)
            LogUtils.eTag(TAG, "toNotifySetting:has exception $intent", e)
            context.startActivity(intent)
        }


    }


}