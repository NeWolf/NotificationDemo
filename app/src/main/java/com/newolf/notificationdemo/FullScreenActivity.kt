package com.newolf.notificationdemo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.newolf.notificationdemo.databinding.ActivityFullScreenBinding

class FullScreenActivity : BaseActivity<ActivityFullScreenBinding>() {
    override fun createBinding(): ActivityFullScreenBinding {
        return ActivityFullScreenBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_full_screen)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


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


}