package com.tablereservation.repository.network

import com.tablereservation.features.customers.Customer
import retrofit2.Call
import retrofit2.http.GET

internal interface TablesAndUsersApi {
    companion object {
        private const val CUSTOMERS = "customer-list.json"
        private const val TABLE_MAP = "table-map.json"
    }

    @GET(CUSTOMERS) fun customers(): Call<List<Customer>>
    @GET(TABLE_MAP) fun tables(): Call<List<Boolean>>
}
