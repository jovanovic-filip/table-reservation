package com.tablereservation.features.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tables")
data class TableEntity(
    @field:PrimaryKey
    val tableId: Int,
    var available: Boolean,
    var reservedForCustomerId: Int = -1
)