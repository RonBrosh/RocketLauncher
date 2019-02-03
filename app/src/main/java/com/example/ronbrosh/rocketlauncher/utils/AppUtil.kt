package com.example.ronbrosh.rocketlauncher.utils

import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.view.Window


class AppUtil {
    companion object {
        private val handlerThread: HandlerThread = HandlerThread("AppUtilHandlerThread")

        init {
            handlerThread.start()
        }

        fun runOnMainThreadAfterDelay(delay: Long, process: () -> Unit) {
            Handler().postDelayed({
                process()
            }, delay)
        }

        fun runOnBackgroundThread(process: () -> Unit) {
            Handler(handlerThread.looper).post(process)
        }

        fun runOnBackgroundThreadAfterDelay(delay: Long, process: () -> Unit) {
            Handler(handlerThread.looper).postDelayed({
                process()
            }, delay)
        }

        fun hideSystemUI(window: Window) {
            // Enables regular immersive mode.
            // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
            // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }
}