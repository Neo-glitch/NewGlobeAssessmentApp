package com.bridge.androidtechnicaltest.pupil.data.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.pupil.domain.repository.PupilRepository

class PupilSyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val pupilRepository: PupilRepository
) : CoroutineWorker(context, workerParams) {
    @SuppressLint("RestrictedApi")

    override suspend fun doWork(): Result {

        return try {
            val response = pupilRepository.syncPupils()

            if (response is Resource.Success) {
                Result.Success()
            } else {
                Result.Failure()
            }

        } catch (e: Exception) {
            Result.failure()
        }


    }
}