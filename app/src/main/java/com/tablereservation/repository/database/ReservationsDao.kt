package com.tablereservation.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tablereservation.features.customers.Customer
import com.tablereservation.features.reservation.Reservation
import com.tablereservation.features.tables.TableEntity

@Dao
interface ReservationsDao {
    @get:Query("SELECT tableId, reservedForCustomerId AS customerId FROM tables WHERE reservedForCustomerId >=0 AND available")
    val reservations: List<Reservation>

    @Query("SELECT * FROM tables WHERE tableId =:id")
    fun getTableByTableId(id : Int) : TableEntity

    @Query("SELECT tableId, reservedForCustomerId AS customerId FROM tables WHERE reservedForCustomerId =:customerId AND available")
    fun getReservationsByCustomer(customerId : Int) : List<Reservation>

    @Query("UPDATE tables SET reservedForCustomerId = :customerId WHERE tableId = :tableId")
    fun insertReservation(tableId:Int, customerId:Int)

    @Query("UPDATE tables SET reservedForCustomerId = -1 WHERE tableId = :tableId")
    fun deleteReservation(tableId:Int)

    @Query("UPDATE tables SET reservedForCustomerId = -1 ")
    fun deleteAllReservations()

    @get:Query("SELECT * FROM customer")
    val customers: List<Customer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCustomers(customers : List<Customer>)

    @get:Query("SELECT * FROM tables")
    val liveTables: LiveData<List<TableEntity>>

    @get:Query("SELECT * FROM tables")
    val tables: List<TableEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceTables(tables : List<TableEntity>)

    @Query("DELETE FROM tables")
    fun deleteAllTables()
}