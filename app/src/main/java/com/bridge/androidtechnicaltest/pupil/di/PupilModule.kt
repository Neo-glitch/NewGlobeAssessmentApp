package com.bridge.androidtechnicaltest.pupil.di

import com.bridge.androidtechnicaltest.core.database.AppDatabase
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilDao
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilLocalDataSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilLocalDataSourceImpl
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.PupilRemoteDataSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.PupilRemoteDataSourceImpl
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.api.PupilApi
import com.bridge.androidtechnicaltest.pupil.data.repository.PupilRepositoryImpl
import com.bridge.androidtechnicaltest.pupil.domain.PupilRepository
import com.bridge.androidtechnicaltest.pupil.presentation.AddEditPupilViewModel
import com.bridge.androidtechnicaltest.pupil.presentation.PupilDetailViewModel
import com.bridge.androidtechnicaltest.pupil.presentation.PupilListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val pupilModule = module {
    single<PupilDao>{ get<AppDatabase>().pupilDao }
    single<PupilApi>{ get<Retrofit>().create(PupilApi::class.java)}

    singleOf(::PupilLocalDataSourceImpl).bind<PupilLocalDataSource>()
    singleOf(::PupilRemoteDataSourceImpl).bind<PupilRemoteDataSource>()

    singleOf(::PupilRepositoryImpl).bind<PupilRepository>()

    viewModelOf(::PupilListViewModel)
    viewModelOf(::PupilDetailViewModel)
    viewModelOf(::AddEditPupilViewModel)
}
