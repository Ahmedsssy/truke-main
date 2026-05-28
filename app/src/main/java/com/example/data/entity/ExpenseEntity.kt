package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val category: String, // جاز / بنزين، صيانة، مرتب سواق إلخ
    val dateMillis: Long,
    val notes: String = "",
    val shipmentId: Int? = null // شحنة مرتبطة (اختياري)
)
