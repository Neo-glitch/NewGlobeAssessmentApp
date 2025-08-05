package com.bridge.androidtechnicaltest.pupil.util

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bridge.androidtechnicaltest.pupil.data.worker.PupilSyncWorker
import java.util.concurrent.TimeUnit

const val ONE_TIME_PUPIL_SYNC_WORK_NAME = "oneTimePupilSyncWork"
const val PERIODIC_PUPIL_SYNC_WORK_NAME = "periodicPupilSyncWork"

fun enqueOneTimePupilSyncWork(context: Context) {
    val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    val workRequest = OneTimeWorkRequestBuilder<PupilSyncWorker>()
        .setConstraints(workConstraints)
        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        ONE_TIME_PUPIL_SYNC_WORK_NAME,
        androidx.work.ExistingWorkPolicy.REPLACE,
        workRequest
    )
}

fun enquePeriodicPupilSyncWork(context: Context) {
    val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()

    val workRequest = PeriodicWorkRequestBuilder<PupilSyncWorker>(
        30, TimeUnit.MINUTES
    )
        .setConstraints(workConstraints)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        PERIODIC_PUPIL_SYNC_WORK_NAME,
        androidx.work.ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}