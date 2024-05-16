package com.newolf.notificationdemo

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.newolf.notificationdemo.adapter.NotificationTypeAdapter
import com.newolf.notificationdemo.app.NotificationConstants
import com.newolf.notificationdemo.data.NotificationBean
import com.newolf.notificationdemo.data.NotificationType
import com.newolf.notificationdemo.databinding.ActivityBasicNotificationBinding
import com.newolf.notificationdemo.utils.NotificationHelper
import com.newolf.notificationdemo.utils.scheduleNotification
import com.newolf.notificationdemo.utils.turnScreenOffAndKeyguardOn
import com.newolf.notificationdemo.vm.BasicNotificationViewModel

class BasicNotificationActivity : BaseActivity<ActivityBasicNotificationBinding>() {

    companion object {
        const val TAG = "BasicNotificationActivity"
    }

    private val notificationTypeAdapter: NotificationTypeAdapter by lazy {
        NotificationTypeAdapter()
    }

    private val viewModel: BasicNotificationViewModel by lazy {
        ViewModelProvider(this)[BasicNotificationViewModel::class.java]
    }

    override fun createBinding(): ActivityBasicNotificationBinding {
        return ActivityBasicNotificationBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_basic_notification)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }


    override fun initView() {
        binding.rivShow.addLifecycleObserver(this)
            .updateRotate(9F)

        binding.rvList.layoutManager =
            LinearLayoutManager(binding.rvList.context, RecyclerView.VERTICAL, false)

        binding.rvList.adapter = notificationTypeAdapter
    }

    override fun getExtraIntent() {
        LogUtils.dTag(TAG, "getExtraIntentData ${intent.extras}")
        intent.extras?.let {
            for (key in it.keySet()) {
                LogUtils.dTag(TAG, "key = $key , value ${it.getString(key)}")
            }
        }
    }


    override fun initData() {
        viewModel.notificationTypeList.observe(/* owner = */ this
        ) { listData -> notificationTypeAdapter.setNewInstance(listData) }
        viewModel.composeData()
    }

    @Suppress("UNCHECKED_CAST")
    override fun initListener() {
        notificationTypeAdapter.setOnItemClickListener { adapter, _, position ->

            if (!NotificationHelper.checkNotificationsEnabled()) {
                showRequestPermissionRationale()
                return@setOnItemClickListener
            }
            val listData: MutableList<NotificationBean> =
                adapter.data as MutableList<NotificationBean>
            when (listData[position].type) {
                NotificationType.BASIC_ADD_ACTION_BUTTONS -> {
                    addActionButton()
                }

                NotificationType.BASIC_DIRECT_REPLY -> {
                    directReply()
                }

                NotificationType.BASIC_PROGRESS_BAR_A -> {
                    progressBarA()
                }

                NotificationType.BASIC_PROGRESS_BAR_B -> {
                    progressBarB()
                }

                NotificationType.BASIC_SYSTEM_WIDE_CATEGORY -> {
                    setSystemWideCategory()
                }

                NotificationType.BASIC_URGENT_MESSAGE -> {
                    urgentNotification()
                }

                NotificationType.BASIC_UPDATE -> {
                    updateNotification()
                }

                NotificationType.BASIC_REMOVE -> {
                    removeNotification()
                }


                else -> {

                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateNotification() {
        val basicNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, NotificationHelper.getMainNotificationId())
        } else {
            NotificationCompat.Builder(this)
        }
        basicNotification.setSmallIcon(R.drawable.tj809)
        basicNotification.setContentTitle("Picture Download")
        basicNotification.setContentText("Download in progress")

        progressNotificationId = NotificationHelper.getSendNotificationId()
        NotificationManagerCompat.from(this).apply {
            basicNotification.setProgress(100, 88, false)

            notify(progressNotificationId, basicNotification.build())
        }
    }

    private fun removeNotification() {
        NotificationHelper.cancelAll()
    }

    private var isLockScreen = false
    private fun urgentNotification() {
        isLockScreen = !isLockScreen
        scheduleNotification(isLockScreen)
        turnScreenOffAndKeyguardOn()
        LogUtils.e("scheduleNotification isLockScreen = $isLockScreen ")
    }

    private fun setSystemWideCategory() {
        val basicNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, NotificationHelper.getMainNotificationId())
        } else {
            NotificationCompat.Builder(this)
        }
        val intent = Intent(this, BasicNotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(
                NotificationConstants.EXTRA_NOTIFICATION_ID,
                NotificationHelper.getSendNotificationId()
            )
        }
        LogUtils.dTag(
            TAG,
            "sendBasicNotification intent = $intent, extra = ${
                intent.getIntExtra(
                    NotificationConstants.EXTRA_NOTIFICATION_ID,
                    -1
                )
            }, id = ${NotificationHelper.getSendNotificationId()}"
        )


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
            .setCategory(NotificationCompat.CATEGORY_CALL)




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            basicNotification.setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }


        NotificationHelper.sendNotification(
            basicNotification.build()
        )
    }

    @SuppressLint("MissingPermission")
    private fun progressBarB() {
        val basicNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, NotificationHelper.getMainNotificationId())
        } else {
            NotificationCompat.Builder(this)
        }
        basicNotification.setSmallIcon(R.drawable.tj809)
        basicNotification.setContentTitle("Picture Download")
        basicNotification.setContentText("Download in progress")

        progressNotificationId = NotificationHelper.getSendNotificationId()
        NotificationManagerCompat.from(this).apply {
            basicNotification.setProgress(0, 50, true)

            notify(progressNotificationId, basicNotification.build())
        }
    }

    private var progressNotificationId: Int = 0

    @SuppressLint("MissingPermission")
    private fun progressBarA() {
        val basicNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, NotificationHelper.getMainNotificationId())
        } else {
            NotificationCompat.Builder(this)
        }
        basicNotification.setSmallIcon(R.drawable.tj809)
        basicNotification.setContentTitle("Picture Download")
        basicNotification.setContentText("Download in progress")

        NotificationHelper.updateNotificationId()
        progressNotificationId = NotificationHelper.getSendNotificationId()
        NotificationManagerCompat.from(this).apply {
            basicNotification.setProgress(100, 50, false)

            notify(progressNotificationId, basicNotification.build())
        }

    }

    private fun directReply() {

    }

    private fun addActionButton() {
        val basicNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, NotificationHelper.getMainNotificationId())
        } else {
            NotificationCompat.Builder(this)
        }
        val intent = Intent(this, BasicNotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(
                NotificationConstants.EXTRA_NOTIFICATION_ID,
                NotificationHelper.getSendNotificationId()
            )
        }
        LogUtils.dTag(
            TAG,
            "sendBasicNotification intent = $intent, extra = ${
                intent.getIntExtra(
                    NotificationConstants.EXTRA_NOTIFICATION_ID,
                    -1
                )
            }, id = ${NotificationHelper.getSendNotificationId()}"
        )


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
//            .setContentIntent(pendingIntent)
            .addAction(
                NotificationCompat.Action(
                    R.drawable.tj809,
                    "Read",
                    pendingIntent
                )
            )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            basicNotification.setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }


        NotificationHelper.sendNotification(
            basicNotification.build()
        )

        ToastUtils.showShort(R.string.send_success)
    }

}