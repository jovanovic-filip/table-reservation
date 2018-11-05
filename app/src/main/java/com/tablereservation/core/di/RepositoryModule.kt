package com.tablereservation.core.di

import android.content.Context
import androidx.room.Room
import com.tablereservation.repository.DefaultRepository
import com.tablereservation.repository.Repository
import com.tablereservation.repository.database.ReservationsDao
import com.tablereservation.repository.database.ReservationsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(appContext : Context) {

    private var reservationsDatabase : ReservationsDatabase = Room.databaseBuilder(
        appContext,
        ReservationsDatabase::class.java,
        "reservations"
    ).build()


    @Provides
    @Singleton
    fun provideDatabase() : ReservationsDatabase {
        return reservationsDatabase
    }

    @Provides
    @Singleton
    fun providesDao( reservationsDatabase : ReservationsDatabase) : ReservationsDao  = reservationsDatabase.reservationDao()

    @Provides
    @Singleton
    fun provideReservationRepository(dataSource: DefaultRepository): Repository = dataSource
}