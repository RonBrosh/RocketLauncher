package com.example.ronbrosh.rocketlauncher.utils

import android.os.Handler
import android.os.HandlerThread


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
    }
}