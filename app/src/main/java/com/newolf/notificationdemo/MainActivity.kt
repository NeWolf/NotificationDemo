package com.newolf.notificationdemo

import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.newolf.notificationdemo.adapter.NotificationTypeAdapter
import com.newolf.notificationdemo.app.NotificationConstants
import com.newolf.notificationdemo.data.NotificationBean
import com.newolf.notificationdemo.data.NotificationType
import com.newolf.notificationdemo.databinding.ActivityMainBinding
import com.newolf.notificationdemo.utils.NotificationHelper
import com.newolf.notificationdemo.vm.MainViewModel
import java.util.UUID


@Suppress("UNCHECKED_CAST")
class MainActivity : BaseActivity<ActivityMainBinding>() {


    private val notificationTypeAdapter: NotificationTypeAdapter by lazy {
        NotificationTypeAdapter()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.registerMainNotificationChannel()
        }
        hasPostNotificationPermission =
            NotificationHelper.requestNotificationsPermission(this, permissionRationaleDialog)
//        showRequestPermissionRationale()


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogUtils.eTag(
            TAG,
            "onNewIntent extras = ${intent?.extras}, extra_notification_id = ${intent?.extras?.get("extra_notification_id")}"
        )
    }

    override fun onRestart() {
        super.onRestart()
        if (!hasPostNotificationPermission) {
            hasPostNotificationPermission =
                NotificationHelper.requestNotificationsPermission(this, permissionRationaleDialog)
        }

    }

    private var hasPostNotificationPermission = false


    override fun initView() {
        binding.rivShow.addLifecycleObserver(this)
            .updateRotate(9F)

        binding.rvList.layoutManager =
            LinearLayoutManager(binding.rvList.context, RecyclerView.VERTICAL, false)

        binding.rvList.adapter = notificationTypeAdapter


    }

    override fun getExtraIntent() {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        NotificationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun initData() {
        viewModel.notificationTypeList.observe(/* owner = */ this
        ) { listData -> notificationTypeAdapter.setNewInstance(listData) }
        viewModel.composeData()
    }

    override fun initListener() {
        notificationTypeAdapter.setOnItemClickListener { adapter, _, position ->

            if (!NotificationHelper.checkNotificationsEnabled()) {
                showRequestPermissionRationale()
                return@setOnItemClickListener
            }
            val listData: MutableList<NotificationBean> =
                adapter.data as MutableList<NotificationBean>
            when (listData[position].type) {
                NotificationType.BASIC_NOTIFICATION -> {
                    sendBasicNotification()
                }

                NotificationType.EXPANDABLE_NOTIFICATION -> {
                    sendExpandableNotification()
                }

                NotificationType.CALL_STYLE_NOTIFICATION -> {
                    sendCallStyleNotification()
                }

                NotificationType.TIME_SENSITIVE_NOTIFICATION -> {
                    sendTimeSensitiveNotification()
                }

                NotificationType.CUSTOM_NOTIFICATION -> {
                    sendCustomNotification()
                }

                else -> {

                }
            }
        }
    }


    private fun sendBasicNotification() {
        val basicNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, NotificationHelper.getMainNotificationId())
        } else {
            NotificationCompat.Builder(this)
        }
        val intent = Intent(this, BasicNotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        intent.putExtra(
            NotificationConstants.EXTRA_NOTIFICATION_ID,
            NotificationHelper.getSendNotificationId()
        )
        LogUtils.dTag(
            TAG,
            "sendBasicNotification intent = $intent, extra = ${
                intent.getIntExtra(
                    NotificationConstants.EXTRA_NOTIFICATION_ID,
                    -1
                )
            }, id = ${NotificationHelper.getSendNotificationId()}"
        )
//        val bundle = Bundle()
//        bundle.putInt(NotificationConstants.EXTRA_NOTIFICATION_ID,
//            NotificationHelper.getSendNotificationId())
//
//        intent.putExtra("extra",bundle)

        val pendingIntent: PendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                System.currentTimeMillis().hashCode(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        basicNotification
            .setSmallIcon(R.drawable.tj809)
            .setContentTitle(getString(R.string.basic_notification_title))
            .setContentText(getString(R.string.basic_notification_content_text))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
//            .addAction(
//                NotificationCompat.Action(
//                    R.drawable.tj809,
//                    "Read",
//                    PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)
//                )
//            )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            basicNotification.setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }


        NotificationHelper.sendNotification(
            basicNotification.build()
        )

        ToastUtils.showShort(R.string.send_success)


    }

    private fun sendExpandableNotification() {

    }

    private fun sendCallStyleNotification() {

    }

    private fun sendTimeSensitiveNotification() {

    }

    private fun sendCustomNotification() {

    }

    override fun finish() {
        super.finish()
        LogUtils.e(Exception())
    }

    companion object {
        private const val TAG: String = "MainActivity"
    }


}