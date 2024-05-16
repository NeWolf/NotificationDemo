package com.newolf.notificationdemo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.newolf.notificationdemo.databinding.ActivityLockScreenBinding
import com.newolf.notificationdemo.utils.turnScreenOffAndKeyguardOn
import com.newolf.notificationdemo.utils.turnScreenOnAndKeyguardOff

class LockScreenActivity : BaseActivity<ActivityLockScreenBinding>() {
    override fun createBinding(): ActivityLockScreenBinding {
        return ActivityLockScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_lock_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        turnScreenOnAndKeyguardOff()
    }

    override fun initView() {
        binding.rivShow.addLifecycleObserver(this)
            .rotation = 9F
    }

    override fun getExtraIntent() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
    }
}