package com.bridge.androidtechnicaltest.di

import com.bridge.androidtechnicaltest.core.database.DatabaseFactory
import com.bridge.androidtechnicaltest.pupil.data.repository.PupilRepositoryImpl
import com.bridge.androidtechnicaltest.pupil.domain.PupilRepository

import org.koin.dsl.module

//val networkModule = module {
////    factory { PupilAPIFactory.retrofitPupil() }
//}

val databaseModule = module {
    single { DatabaseFactory.getDBInstance(get()) }
//    single<PupilRepository>{ PupilRepositoryImpl(get(), get()) }
}

