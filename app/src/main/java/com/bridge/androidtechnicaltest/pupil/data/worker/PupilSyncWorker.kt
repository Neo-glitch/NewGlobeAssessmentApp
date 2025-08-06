package com.bridge.androidtechnicaltest.pupil.data.worker

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.bridge.androidtechnicaltest.core.domain.Resource
import com.bridge.androidtechnicaltest.core.utils.NotificationHelper
import com.bridge.androidtechnicaltest.pupil.domain.repository.PupilRepository

class PupilSyncWorker(
    private val context: Context,
    workerParams: WorkerParameters,
    private val pupilRepository: PupilRepository
) : CoroutineWorker(context, workerParams) {

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val syncNotificationHelper = NotificationHelper(context = context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setForeground(
                ForegroundInfo(
                    NotificationHelper.SYNC_FOREGROUND_INFO_NOTIFICATION_ID,
                    syncNotificationHelper.createSyncNotification(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            )
        } else {
            setForeground(
                ForegroundInfo(
                    NotificationHelper.SYNC_FOREGROUND_INFO_NOTIFICATION_ID,
                    syncNotificationHelper.createSyncNotification(),
                )
            )
        }

        return try {
            val response = pupilRepository.syncPupils()

            if (response is Resource.Success) {
                Result.Success()
            } else {
                Result.Retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

}