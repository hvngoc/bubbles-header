package com.bubbles.head.chat

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bubbles.head.chat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val ACTION_STOP_FOREGROUND = "${BuildConfig.APPLICATION_ID}.stopfloating.service"
    }

    private val settingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (!checkHasDrawOverlayPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val setting = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${BuildConfig.APPLICATION_ID}")
                )
                settingLauncher.launch(setting)
            }

        }

        binding.start.setOnClickListener {
            startService(Intent(this, FloatingControlService::class.java))

        }
        binding.stop.setOnClickListener {
            val stop = Intent(this, FloatingControlService::class.java).apply {
                action = ACTION_STOP_FOREGROUND
            }
            startService(stop)
        }
    }

    private fun checkHasDrawOverlayPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }
}