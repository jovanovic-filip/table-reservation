package com.tablereservation.repository

import androidx.lifecycle.LiveData
import com.tablereservation.core.exception.Failure
import com.tablereservation.core.functional.Either
import com.tablereservation.core.interactor.UseCase
import com.tablereservation.features.customers.Customer
import com.tablereservation.features.reservation.Reservation
import com.tablereservation.features.tables.TableEntity

interface Repository {
    fun customers() : Either<Failure, List<Customer>>
    fun tables() : Either<Failure, LiveData<List<TableEntity>>>
    fun getTable(tableId : Int) : TableEntity

    fun clearReservations() : Either<Failure, UseCase.None>
    fun getReservations(customerId : Int) : List<Reservation>
    fun saveReservation(reservation : Reservation)
    fun deleteReservation(tableId : Int)
}