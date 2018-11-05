package com.tablereservation

import android.content.Context
import androidx.room.Room
import com.tablereservation.features.customers.Customer
import com.tablereservation.repository.database.ReservationsDao
import com.tablereservation.repository.database.ReservationsDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Arrays.asList
import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tablereservation.features.reservation.Reservation
import com.tablereservation.features.tables.TableEntity

import com.google.common.truth.Truth.*

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

   lateinit var database : ReservationsDatabase
   lateinit var databaseDao : ReservationsDao

    val dummyCustomers = asList(
        Customer(1,"Tom","Sawyer"),
        Customer(2,"Huckleberry","Finn"),
        Customer(3,"Mary","Jane")
    )

    val tableA = TableEntity(5,true,3)
    val tableB = TableEntity(3,true,3)
    val tableC = TableEntity(7,true,1)
    val tableD = TableEntity(4,false,-1)

    val dummyTables = asList(
        TableEntity(8,false,-1),
        TableEntity(1,false,-1),
        TableEntity(0,true,-1),
        tableA,
        TableEntity(2,false,-1),
        tableD,
        tableC,
        TableEntity(9,true,-1),
        TableEntity(6,false,-1),
        tableB
    )

    val dummyReservations = asList(
        Reservation(5,3),
        Reservation(7,1),
        Reservation(3,3)
    )

    @Before
    fun initDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context,
            ReservationsDatabase::class.java).build()
        databaseDao = database.reservationDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun customersWriteReadTest(){
        databaseDao.insertAllCustomers(dummyCustomers)
        assertThat( databaseDao.customers).containsAllIn(dummyCustomers)
    }

    @Test
    fun tablesWriteReadTest(){
        assertThat(databaseDao.tables).isEmpty()
        databaseDao.insertOrReplaceTables(dummyTables)
        assertThat(databaseDao.tables).containsAllIn(dummyTables)
    }

    @Test
    fun tablesClearTest(){
        databaseDao.insertOrReplaceTables(dummyTables)
        databaseDao.deleteAllTables()
        assertThat(databaseDao.tables).isEmpty()
    }

    @Test
    fun getReservationsTest(){
        databaseDao.insertOrReplaceTables(dummyTables)
        val reservations = databaseDao.reservations
        assertThat(reservations).isNotEmpty()
        assertThat(reservations).containsAllIn(dummyReservations)
        assertThat(reservations.size).isEqualTo(3)
    }

    @Test
    fun getTableByIdTest(){
        databaseDao.insertOrReplaceTables(dummyTables)
        assertThat(databaseDao.getTableByTableId(4)).isEqualTo(tableD)
        assertThat(databaseDao.getTableByTableId(7)).isEqualTo(tableC)
        assertThat(databaseDao.getTableByTableId(99)).isNull()
    }

    @Test
    fun getReservationsByCustomerIdTest(){
        databaseDao.insertOrReplaceTables(dummyTables)
        assertThat(databaseDao.getReservationsByCustomer(4)).isEmpty()
        assertThat(databaseDao.getReservationsByCustomer(3).size).isEqualTo(2)
        assertThat(databaseDao.getReservationsByCustomer(1).size).isEqualTo(1)
    }

    @Test
    fun insertReservationTest(){
        databaseDao.insertOrReplaceTables(dummyTables)
        assertThat(databaseDao.getReservationsByCustomer(4)).isEmpty()
        databaseDao.insertReservation(9, 4)
        assertThat(databaseDao.getReservationsByCustomer(4))
            .isEqualTo(listOf(Reservation(9,4)))
    }

    @Test
    fun deleteSingleReservationTest(){
        databaseDao.insertOrReplaceTables(dummyTables)
        assertThat(databaseDao.getReservationsByCustomer(3).size).isEqualTo(2)
        databaseDao.deleteReservation(5)
        assertThat(databaseDao.getReservationsByCustomer(3).size).isEqualTo(1)
        databaseDao.deleteReservation(3)
        assertThat(databaseDao.getReservationsByCustomer(3)).isEmpty()
    }

    @Test
    fun deleteAllReservationsTest(){
        databaseDao.insertOrReplaceTables(dummyTables)
        assertThat(databaseDao.getReservationsByCustomer(3).size).isEqualTo(2)
        databaseDao.deleteAllReservations()
        assertThat(databaseDao.getReservationsByCustomer(3)).isEmpty()
    }
}