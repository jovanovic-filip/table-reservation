package com.tablereservation.features.customers

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Customer(
    @field:PrimaryKey
    val id: Int,
    val customerFirstName: String,
    val customerLastName: String
)