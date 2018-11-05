package com.tablereservation.features.reservation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reservation(
    @field:PrimaryKey
    val tableId: Int,
    val customerId: Int
)