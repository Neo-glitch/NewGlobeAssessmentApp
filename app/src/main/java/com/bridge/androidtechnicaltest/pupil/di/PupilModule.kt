package com.bridge.androidtechnicaltest.pupil.di

import com.bridge.androidtechnicaltest.core.database.AppDatabase
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilDao
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilLocalDataSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilLocalDataSourceImpl
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.PupilRemoteDataSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.PupilRemoteDataSourceImpl
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.api.PupilApi
import com.bridge.androidtechnicaltest.pupil.data.repository.PupilRepositoryImpl
import com.bridge.androidtechnicaltest.pupil.data.worker.PupilSyncWorker
import com.bridge.androidtechnicaltest.pupil.domain.repository.PupilRepository
import com.bridge.androidtechnicaltest.pupil.domain.usecases.AddUpdatePupilUseCase
import com.bridge.androidtechnicaltest.pupil.domain.usecases.DeletePupilUseCase
import com.bridge.androidtechnicaltest.pupil.domain.usecases.GetPupilUseCase
import com.bridge.androidtechnicaltest.pupil.domain.usecases.GetPupilsUseCase
import com.bridge.androidtechnicaltest.pupil.presentation.add_edit_pupil.AddEditPupilViewModel
import com.bridge.androidtechnicaltest.pupil.presentation.pupil_detail.PupilDetailViewModel
import com.bridge.androidtechnicaltest.pupil.presentation.pupils.PupilListViewModel
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val pupilModule = module {
    workerOf(::PupilSyncWorker)
    single<PupilDao>{ get<AppDatabase>().pupilDao }
    single<PupilApi>{ get<Retrofit>().create(PupilApi::class.java)}

    singleOf(::PupilLocalDataSourceImpl).bind<PupilLocalDataSource>()
    singleOf(::PupilRemoteDataSourceImpl).bind<PupilRemoteDataSource>()
    singleOf(::PupilRepositoryImpl).bind<PupilRepository>()

    factoryOf(::AddUpdatePupilUseCase)
    factoryOf(::DeletePupilUseCase)
    factoryOf(::GetPupilsUseCase)
    factoryOf(::GetPupilUseCase)

    viewModelOf(::PupilListViewModel)
    viewModelOf(::PupilDetailViewModel)
    viewModelOf(::AddEditPupilViewModel)
}
