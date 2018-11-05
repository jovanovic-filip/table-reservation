package com.tablereservation.features.tables


data class DisplayTable (
     val tableId : Int,
     val available : Boolean,
     val customerInitials : String = ""
)