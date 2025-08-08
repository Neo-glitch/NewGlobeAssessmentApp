package com.bridge.androidtechnicaltest.pupil.util

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bridge.androidtechnicaltest.pupil.data.worker.PupilSyncWorker
import java.util.concurrent.TimeUnit


class PupilSyncWorkManager(private val context: Context) {

    companion object {
        private const val ONE_TIME_PUPIL_SYNC_WORK_NAME = "oneTimePupilSyncWork"
        private const val PERIODIC_PUPIL_SYNC_WORK_NAME = "periodicPupilSyncWork"
    }

    fun enqueOneTimePupilSyncWork() {
        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<PupilSyncWorker>()
            .setConstraints(workConstraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                30,
                TimeUnit.SECONDS
            )
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            ONE_TIME_PUPIL_SYNC_WORK_NAME,
            androidx.work.ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun enquePeriodicPupilSyncWork() {
        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<PupilSyncWorker>(
            20, TimeUnit.MINUTES
        )
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                30,
                TimeUnit.SECONDS
            )
            .setConstraints(workConstraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            PERIODIC_PUPIL_SYNC_WORK_NAME,
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
