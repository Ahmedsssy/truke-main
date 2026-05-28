package com.example.data

import com.example.data.dao.TransportDao
import com.example.data.entity.ExpenseEntity
import com.example.data.entity.ShipmentEntity
import kotlinx.coroutines.flow.Flow

class TransportRepository(private val transportDao: TransportDao) {
    val allShipments: Flow<List<ShipmentEntity>> = transportDao.getAllShipments()
    val allExpenses: Flow<List<ExpenseEntity>> = transportDao.getAllExpenses()

    suspend fun insertShipment(shipment: ShipmentEntity): Long {
        return transportDao.insertShipment(shipment)
    }

    suspend fun updateShipment(shipment: ShipmentEntity) {
        transportDao.updateShipment(shipment)
    }

    suspend fun deleteShipment(shipment: ShipmentEntity) {
        transportDao.deleteShipment(shipment)
    }

    suspend fun getShipmentById(id: Int): ShipmentEntity? {
        return transportDao.getShipmentById(id)
    }

    suspend fun insertExpense(expense: ExpenseEntity): Long {
        return transportDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        transportDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        transportDao.deleteExpense(expense)
    }

    fun getExpensesForShipment(shipmentId: Int): Flow<List<ExpenseEntity>> {
        return transportDao.getExpensesForShipment(shipmentId)
    }
}
