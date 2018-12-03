
package com.tablereservation.core.di

import com.tablereservation.AndroidApplication
import com.tablereservation.core.di.viewmodel.ViewModelModule
import com.tablereservation.core.platform.MainActivity
import com.tablereservation.features.customers.CustomersListFragment
import com.tablereservation.features.reservation.ClearReservationsWorker
import com.tablereservation.features.tables.TableSelectionFragment
import com.tablereservation.repository.Repository
import com.tablereservation.repository.database.ReservationsDao
import com.tablereservation.repository.database.ReservationsDatabase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, RepositoryModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun repository(): Repository
    fun reservationsDatabase(): ReservationsDatabase
    fun reservationsDao(): ReservationsDao

    fun inject(application: AndroidApplication)
    fun inject(mainActivity: MainActivity)
    fun inject(customersListFragment: CustomersListFragment)
    fun inject(TableSelectionFragment: TableSelectionFragment)
    fun inject(clearReservationsWorker: ClearReservationsWorker)
}
