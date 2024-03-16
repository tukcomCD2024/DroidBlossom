package com.droidblossom.archive.util

import com.droidblossom.archive.presentation.ui.ErrorActivity

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Process
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Integer.max

class ExceptionHandler(
    application: Application,
    private val crashlyticsExceptionHandler: Thread.UncaughtExceptionHandler
) : Thread.UncaughtExceptionHandler {

    private var lastActivity: Activity? = null
    private var activityCount = 0

    init {
        application.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacks() {

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    if (isSkipActivity(activity)) {
                        return
                    }
                    lastActivity = activity
                }

                override fun onActivityStarted(activity: Activity) {
                    if (isSkipActivity(activity)) {
                        return
                    }
                    activityCount++
                    lastActivity = activity
                }

                override fun onActivityStopped(activity: Activity) {
                    if (isSkipActivity(activity)) {
                        return
                    }
                    activityCount--
                    activityCount = max(0, activityCount)
                }

                override fun onActivityDestroyed(activity: Activity) {
                    if (activity === lastActivity) {
                        lastActivity = null
                    }
                }
            })
    }

    private fun isSkipActivity(activity: Activity) = activity is ErrorActivity

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        lastActivity?.let {
            val stringWriter = StringWriter()
            throwable.printStackTrace(PrintWriter(stringWriter))

            startErrorActivity(it, stringWriter.toString())
        }
        crashlyticsExceptionHandler.uncaughtException(thread, throwable)
        Process.killProcess(Process.myPid())
        System.exit(-1)
    }

    private fun startErrorActivity(activity: Activity, errorText: String) {
        val errorActivityIntent = Intent(activity, ErrorActivity::class.java).apply {
            putExtra(ErrorActivity.EXTRA_INTENT, activity.intent)
            putExtra(ErrorActivity.EXTRA_ERROR_TEXT, errorText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        activity.startActivity(errorActivityIntent)
        activity.finish()
    }
}