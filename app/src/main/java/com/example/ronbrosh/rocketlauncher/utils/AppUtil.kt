package com.example.ronbrosh.rocketlauncher.utils

import android.os.Handler
import android.os.HandlerThread
import android.util.Log


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
    }
}