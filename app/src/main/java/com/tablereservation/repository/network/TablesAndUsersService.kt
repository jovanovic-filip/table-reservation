package com.tablereservation.repository.network

import com.tablereservation.features.customers.Customer
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TablesAndUsersService
@Inject constructor(retrofit: Retrofit) : TablesAndUsersApi {

    private val tablesAndUsersApi by lazy { retrofit.create(TablesAndUsersApi::class.java) }

    override fun customers(): Call<List<Customer>> = tablesAndUsersApi.customers()

    override fun tables(): Call<List<Boolean>>  = tablesAndUsersApi.tables()

}
