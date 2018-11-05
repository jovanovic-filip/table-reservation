package com.tablereservation.core.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tablereservation.features.customers.CustomersViewModel
import com.tablereservation.features.tables.TablesListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CustomersViewModel::class)
    abstract fun bindsCustomersListViewModel(customersViewModel: CustomersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TablesListViewModel::class)
    abstract fun bindsTablesListViewModel(tablesViewModel: TablesListViewModel): ViewModel

}