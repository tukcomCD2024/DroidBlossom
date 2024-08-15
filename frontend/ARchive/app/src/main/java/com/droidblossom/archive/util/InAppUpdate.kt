package com.droidblossom.archive.util

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.droidblossom.archive.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdate(private val activity: Activity) : InstallStateUpdatedListener {

    private var appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)
    private var currentType = AppUpdateType.IMMEDIATE

    companion object {
        const val TAG = "InAppUpdate"
        const val MY_REQUEST_CODE = 500
        const val IN_APP_UPDATE_RECEIVER = "InAppUpdateReceiver"
    }

    init {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    startUpdate(info, AppUpdateType.IMMEDIATE)
                } else if (info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    startUpdate(info, AppUpdateType.FLEXIBLE)
                }
            } else {
                sendUpdateFailureBroadcast()
            }
            appUpdateManager.registerListener(this)
        }
    }

    private fun startUpdate(info: AppUpdateInfo, type: Int) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                info,
                type,
                activity,
                MY_REQUEST_CODE
            )
            currentType = type
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Update flow failed", e)
            sendUpdateFailureBroadcast()
        }
    }

    fun onResume() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (currentType == AppUpdateType.FLEXIBLE && appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                flexibleUpdateDownloadCompleted()
            } else if (currentType == AppUpdateType.IMMEDIATE && appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE)
            }
        }
    }

    private fun flexibleUpdateDownloadCompleted() {
        Log.d(TAG, "유연 업데이트 다운로드 완료. 앱을 재시작합니다.")
        restartApp()
    }

    private fun restartApp() {
        val intent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.finish()
        Runtime.getRuntime().exit(0)
    }

    fun onDestroy() {
        appUpdateManager.unregisterListener(this)
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            flexibleUpdateDownloadCompleted()
        }
    }

    private fun sendUpdateFailureBroadcast() {
        val intent = Intent(IN_APP_UPDATE_RECEIVER)
        val broadcastManager = LocalBroadcastManager.getInstance(activity)
        intent.putExtra("flag", false)
        broadcastManager.sendBroadcast(intent)
    }
}