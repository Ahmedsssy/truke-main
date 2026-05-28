package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shipments")
data class ShipmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerName: String,
    val amount: Double,
    val dateMillis: Long,
    val notes: String = "",
    val origin: String = "",
    val destination: String = ""
)
