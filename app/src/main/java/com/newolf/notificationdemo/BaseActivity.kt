package com.newolf.notificationdemo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.newolf.notificationdemo.utils.NotificationHelper

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
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    protected val binding: T by lazy { createBinding() }


    abstract fun createBinding(): T

    protected val permissionRationaleDialog: AlertDialog by lazy { initPermissionRationaleDialog() }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        getExtraIntent()
        initData()
        initListener()
    }

    abstract fun initView()

    abstract fun getExtraIntent()

    abstract fun initData()

    abstract fun initListener()

    protected fun showRequestPermissionRationale() {
        permissionRationaleDialog.show()
    }

    private fun initPermissionRationaleDialog(): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle(R.string.request_post_permission_title)
            .setMessage(R.string.request_post_permission_rationale)
            .setCancelable(false)
            .setPositiveButton(
                R.string.request_post_permission_positive
            ) { dialog, _ ->

                dialog.dismiss()
                NotificationHelper.toNotifySetting(this)


            }
            .setNegativeButton(
                R.string.request_post_permission_negative
            ) { dialog, _ -> dialog.dismiss() }
            .create()
    }
}