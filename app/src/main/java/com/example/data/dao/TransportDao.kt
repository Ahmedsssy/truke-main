package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.ExpenseEntity
import com.example.data.entity.ShipmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransportDao {
    // Shipments (Revenues)
    @Query("SELECT * FROM shipments ORDER BY dateMillis DESC")
    fun getAllShipments(): Flow<List<ShipmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShipment(shipment: ShipmentEntity): Long

    @Update
    suspend fun updateShipment(shipment: ShipmentEntity)

    @Delete
    suspend fun deleteShipment(shipment: ShipmentEntity)

    @Query("SELECT * FROM shipments WHERE id = :id")
    suspend fun getShipmentById(id: Int): ShipmentEntity?

    // Expenses
    @Query("SELECT * FROM expenses ORDER BY dateMillis DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE shipmentId = :shipmentId ORDER BY dateMillis DESC")
    fun getExpensesForShipment(shipmentId: Int): Flow<List<ExpenseEntity>>
}
