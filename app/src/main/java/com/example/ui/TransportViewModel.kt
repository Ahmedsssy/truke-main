package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ShahnatiApplication
import com.example.data.entity.ExpenseEntity
import com.example.data.entity.ShipmentEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransportViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as ShahnatiApplication).repository

    val shipments = repository.allShipments.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val expenses = repository.allExpenses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Financial calculations
    val totalRevenue: StateFlow<Double> = shipments.map { list ->
        list.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpenses: StateFlow<Double> = expenses.map { list ->
        list.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val netProfit: StateFlow<Double> = combine(totalRevenue, totalExpenses) { rev, exp ->
        rev - exp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Breakdown by Category
    val expensesByCategory: StateFlow<Map<String, Double>> = expenses.map { list ->
        list.groupBy { it.category }.mapValues { entry -> entry.value.sumOf { it.amount } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Monthly earnings mapping: "YYYY-MM" -> Pair(Revenue, Expenses)
    val monthlyFinances: StateFlow<Map<String, Pair<Double, Double>>> = combine(shipments, expenses) { shipList, expList ->
        val dateFormat = SimpleDateFormat("yyyy-MM", Locale.US)
        val finances = mutableMapOf<String, Pair<Double, Double>>()

        shipList.forEach { ship ->
            val monthKey = dateFormat.format(Date(ship.dateMillis))
            val current = finances[monthKey] ?: Pair(0.0, 0.0)
            finances[monthKey] = Pair(current.first + ship.amount, current.second)
        }

        expList.forEach { exp ->
            val monthKey = dateFormat.format(Date(exp.dateMillis))
            val current = finances[monthKey] ?: Pair(0.0, 0.0)
            finances[monthKey] = Pair(current.first, current.second + exp.amount)
        }

        finances.toList().sortedBy { it.first }.toMap()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Database Actions
    fun addShipment(customerName: String, amount: Double, dateMillis: Long, notes: String, origin: String, destination: String) {
        viewModelScope.launch {
            repository.insertShipment(
                ShipmentEntity(
                    customerName = customerName,
                    amount = amount,
                    dateMillis = dateMillis,
                    notes = notes,
                    origin = origin,
                    destination = destination
                )
            )
        }
    }

    fun updateShipment(shipment: ShipmentEntity) {
        viewModelScope.launch {
            repository.updateShipment(shipment)
        }
    }

    fun deleteShipment(shipment: ShipmentEntity) {
        viewModelScope.launch {
            repository.deleteShipment(shipment)
        }
    }

    fun addExpense(amount: Double, category: String, dateMillis: Long, notes: String, shipmentId: Int?) {
        viewModelScope.launch {
            repository.insertExpense(
                ExpenseEntity(
                    amount = amount,
                    category = category,
                    dateMillis = dateMillis,
                    notes = notes,
                    shipmentId = shipmentId
                )
            )
        }
    }

    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    // Share report via standard CSV text format (Excel)
    fun shareLedgerAsCSV(context: Context, reportType: String) {
        viewModelScope.launch {
            val sFormat = SimpleDateFormat("yyyy/MM/dd", Locale.forLanguageTag("ar-EG"))
            val dt = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val sb = java.lang.StringBuilder()

            val filename: String
            when (reportType) {
                "shipments" -> {
                    filename = "تقرير_الشحنات_$dt.csv"
                    sb.append("رقم الشحنة,العميل,القيمة (ج.م),التاريخ,المصدر,الوجهة,الملاحظات\n")
                    shipments.value.forEach {
                        sb.append("${it.id},${it.customerName.replace(",", " ")},${it.amount},${sFormat.format(Date(it.dateMillis))},${it.origin.replace(",", " ")},${it.destination.replace(",", " ")},${it.notes.replace(",", " ")}\n")
                    }
                }
                "expenses" -> {
                    filename = "تقرير_المصاريف_$dt.csv"
                    sb.append("رقم المصروف,القيمة,الفئة,التاريخ,شحنة مرتبطة,الملاحظات\n")
                    expenses.value.forEach {
                        val boundShipment = if (it.shipmentId != null) "#${it.shipmentId}" else "عام"
                        sb.append("${it.id},${it.amount},${it.category},${sFormat.format(Date(it.dateMillis))},$boundShipment,${it.notes.replace(",", " ")}\n")
                    }
                }
                else -> {
                    filename = "الحسابات_المالية_الشاملة_$dt.csv"
                    sb.append("--- ملخص مالي عام ---\n")
                    sb.append("إجمالي الإيرادات,${totalRevenue.value},ج.م\n")
                    sb.append("إجمالي المصروفات,${totalExpenses.value},ج.م\n")
                    sb.append("صافي الأرباح,${netProfit.value},ج.م\n\n")

                    sb.append("--- جدول الشحنات ---\n")
                    sb.append("رقم الشحنة,العميل,القيمة (ج.م),التاريخ,المصدر,الوجهة,الملاحظات\n")
                    shipments.value.forEach {
                        sb.append("${it.id},${it.customerName.replace(",", " ")},${it.amount},${sFormat.format(Date(it.dateMillis))},${it.origin.replace(",", " ")},${it.destination.replace(",", " ")},${it.notes.replace(",", " ")}\n")
                    }

                    sb.append("\n--- جدول المصاريف ---\n")
                    sb.append("رقم المصروف,القيمة,الفئة,التاريخ,شحنة مرتبطة,الملاحظات\n")
                    expenses.value.forEach {
                        val boundShipment = if (it.shipmentId != null) "#${it.shipmentId}" else "عام"
                        sb.append("${it.id},${it.amount},${it.category},${sFormat.format(Date(it.dateMillis))},$boundShipment,${it.notes.replace(",", " ")}\n")
                    }
                }
            }

            try {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_SUBJECT, filename.replace(".csv", ""))
                    putExtra(Intent.EXTRA_TEXT, sb.toString())
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "تخصيص ومشاركة التقرير Excel-CSV")
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(shareIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Share report via Arabic formatted printable text (WhatsApp/SMS)
    fun shareFormattedTextReport(context: Context) {
        val sFormat = SimpleDateFormat("yyyy/MM/dd", Locale.forLanguageTag("ar-EG"))
        val dateStr = sFormat.format(Date())
        val sb = java.lang.StringBuilder()

        sb.append("🚛 *تقرير الشحنات والمصاريف - تطبيق شاحنتي*\n")
        sb.append("🗓️ *تاريخ الإرسال:* $dateStr\n")
        sb.append("=========================\n\n")

        sb.append("📈 *ملخص الحسابات المالي:*\n")
        sb.append("💸 *إجمالي الإيرادات:* ${String.format(Locale.US, "%,.2f", totalRevenue.value)} ج.م\n")
        sb.append("📉 *إجمالي المصروفات:* ${String.format(Locale.US, "%,.2f", totalExpenses.value)} ج.م\n")
        sb.append("💰 *صافي الأرباح:* ${String.format(Locale.US, "%,.2f", netProfit.value)} ج.م\n")
        if (netProfit.value < 0) {
            sb.append("❌ *يرجى الحظر: صافي خسارة حالياً!*\n")
        } else {
            sb.append("🎉 *صافي ربح ممتاز!*\n")
        }
        sb.append("=========================\n\n")

        sb.append("🚚 *آخر 5 شحنات إيرادات*\n")
        val latestShipments = shipments.value.take(5)
        if (latestShipments.isEmpty()) {
            sb.append("لا توجد شحنات مسجلة حالياً.\n")
        } else {
            latestShipments.forEach {
                sb.append("• *العميل:* ${it.customerName} | *المبلغ:* ${it.amount} ج.م\n")
                if (it.origin.isNotEmpty() || it.destination.isNotEmpty()) {
                    sb.append("  🗺️ ${it.origin} ⬅️ ${it.destination}\n")
                }
            }
        }

        sb.append("\n💸 *آخر 5 مصروفات*\n")
        val latestExpenses = expenses.value.take(5)
        if (latestExpenses.isEmpty()) {
            sb.append("لا توجد مصاريف مسجلة حالياً.\n")
        } else {
            latestExpenses.forEach {
                sb.append("• *أمر الصرف:* ${it.category} | *المبلغ:* ${it.amount} ج.م\n")
                if (it.notes.isNotEmpty()) {
                    sb.append("  💬 ${it.notes}\n")
                }
            }
        }

        sb.append("\nتم الإرسال من تطبيق الجوال *شاحنتي لإدارة النقل* 📱")

        try {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, sb.toString())
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "مشاركة التقرير المالي على واتساب")
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
