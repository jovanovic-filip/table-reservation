package com.tablereservation.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tablereservation.features.customers.Customer
import com.tablereservation.features.tables.TableEntity

@Database(entities = [TableEntity::class, Customer::class], version = 1)
abstract class ReservationsDatabase : RoomDatabase() {
    abstract fun reservationDao(): ReservationsDao
}