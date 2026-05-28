package com.example.ui

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CarCrash
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entity.ExpenseEntity
import com.example.data.entity.ShipmentEntity
import com.example.ui.theme.MidnightBlue
import com.example.ui.theme.ActiveBlue
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.PureWhite
import com.example.ui.theme.JetBlack
import com.example.ui.theme.GrayText
import com.example.ui.theme.LightBlueContainer
import com.example.ui.theme.HighContrastBlue
import com.example.ui.theme.LightRedContainer
import com.example.ui.theme.DeepRed
import com.example.ui.theme.LightGreenContainer
import com.example.ui.theme.DeepGreen
import com.example.ui.theme.BorderGray
import com.example.ui.theme.NavigationGray
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Categories mapped directly in Arabic for SQLite storing & immediate rendering
private val ARABIC_EXPENSE_CATEGORIES = listOf(
    "وقود / جاز",
    "صيانة وإصلاح",
    "مرتب السائق",
    "أجور عمال (أنفار)",
    "رسوم طريق / كارتة",
    "مصاريف شحن وتفريغ",
    "مشتريات وقطع غيار",
    "أخرى / متفرقة"
)

private fun getCategoryIcon(cat: String): ImageVector {
    return when (cat) {
        "وقود / جاز" -> Icons.Filled.LocalGasStation
        "صيانة وإصلاح" -> Icons.Filled.Build
        "مرتب السائق" -> Icons.Filled.Person
        "أجور عمال (أنفار)" -> Icons.Filled.Group
        "رسوم طريق / كارتة" -> Icons.Filled.Navigation
        "مصاريف شحن وتفريغ" -> Icons.Filled.LocalShipping
        "مشتريات وقطع غيار" -> Icons.Filled.CarCrash
        else -> Icons.Filled.ReceiptLong
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportAppLayout(viewModel: TransportViewModel) {
    val context = LocalContext.current
    var currentTab by remember { mutableIntStateOf(0) }

    // Dialog state controllers
    var showAddShipmentDialog by remember { mutableStateOf(false) }
    var showAddExpenseDialog by remember { mutableStateOf(false) }

    val shipments by viewModel.shipments.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    // Enforce localized RTL direction for the cargo app so Arabic aligns perfectly on any device
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    color = SlateBackground
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "مرحباً بك",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GrayText,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "ناقل | الإدارة المالية",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MidnightBlue
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(LightBlueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "أ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MidnightBlue
                            )
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = NavigationGray,
                    modifier = Modifier.navigationBarsPadding(),
                    windowInsets = WindowInsets(0, 0, 0, 0)
                ) {
                    val items = listOf(
                        Triple("لوحة التحكم", Icons.Filled.Analytics, 0),
                        Triple("الشحنات", Icons.Filled.LocalShipping, 1),
                        Triple("المصاريف", Icons.Filled.ReceiptLong, 2),
                        Triple("التقارير", Icons.Filled.Assessment, 3)
                    )

                    items.forEach { (label, icon, index) ->
                        NavigationBarItem(
                            selected = currentTab == index,
                            onClick = { currentTab = index },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (currentTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MidnightBlue,
                                selectedTextColor = MidnightBlue,
                                unselectedIconColor = GrayText.copy(alpha = 0.7f),
                                unselectedTextColor = GrayText.copy(alpha = 0.7f),
                                indicatorColor = LightBlueContainer
                            ),
                            modifier = Modifier.testTag("nav_tab_$index")
                        )
                    }
                }
            },
            floatingActionButton = {
                // FAB to quickly add transactions
                if (currentTab == 1 || currentTab == 2) {
                    FloatingActionButton(
                        onClick = {
                            if (currentTab == 1) {
                                showAddShipmentDialog = true
                            } else {
                                showAddExpenseDialog = true
                            }
                        },
                        containerColor = MidnightBlue,
                        contentColor = PureWhite,
                        modifier = Modifier.testTag("add_fab")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (currentTab == 1) "إضافة شحنة" else "إضافة مصروف",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(SlateBackground)
            ) {
                // Active Screen routing
                when (currentTab) {
                    0 -> DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToTab = { currentTab = it },
                        onAddShipment = { showAddShipmentDialog = true },
                        onAddExpense = { showAddExpenseDialog = true }
                    )
                    1 -> ShipmentsScreen(viewModel = viewModel)
                    2 -> ExpensesScreen(viewModel = viewModel)
                    3 -> ReportsScreen(viewModel = viewModel)
                }

                // Add Shipment Dialog
                if (showAddShipmentDialog) {
                    AddShipmentDialog(
                        onDismiss = { showAddShipmentDialog = false },
                        onConfirm = { name, amt, date, origin, dest, notes ->
                            viewModel.addShipment(name, amt, date, notes, origin, dest)
                            showAddShipmentDialog = false
                            Toast.makeText(context, "تم حفظ الشحنة بنجاح 🚚", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // Add Expense Dialog
                if (showAddExpenseDialog) {
                    AddExpenseDialog(
                        shipmentsList = shipments,
                        onDismiss = { showAddExpenseDialog = false },
                        onConfirm = { amt, cat, date, notes, boundShipId ->
                            viewModel.addExpense(amt, cat, date, notes, boundShipId)
                            showAddExpenseDialog = false
                            Toast.makeText(context, "تم حفظ المصروف بنجاح 💸", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    viewModel: TransportViewModel,
    onNavigateToTab: (Int) -> Unit,
    onAddShipment: () -> Unit,
    onAddExpense: () -> Unit
) {
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val netProfit by viewModel.netProfit.collectAsState()
    val categoryExpenses by viewModel.expensesByCategory.collectAsState()
    val shipments by viewModel.shipments.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary Hero Card Matching design guidelines exactly
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("summary_hero_card"),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightBlue),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "صافي الربح الحالي",
                                color = LightBlueContainer.copy(alpha = 0.9f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = String.format(Locale.US, "%,.0f", netProfit),
                                    color = PureWhite,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Light,
                                    letterSpacing = (-1).sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ج.م",
                                    color = LightBlueContainer,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                        }

                        // Accent Icon inside HighContrastBlue rounded container
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(HighContrastBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (netProfit >= 0) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                                contentDescription = null,
                                tint = LightBlueContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(PureWhite.copy(alpha = 0.1f))
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Revenue Column
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "إجمالي الإيرادات",
                                color = PureWhite.copy(alpha = 0.6f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "+${String.format(Locale.US, "%,.0f", totalRevenue)}",
                                color = Color(0xFF80FF80), // Premium bright success green
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Subtle vertical divider with spacers
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(PureWhite.copy(alpha = 0.15f))
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        // Expense Column
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "إجمالي المصاريف",
                                color = PureWhite.copy(alpha = 0.6f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "-${String.format(Locale.US, "%,.0f", totalExpenses)}",
                                color = Color(0xFFFF8080), // Soft diagnostic expense red
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Quick Action Grid conforming to design template
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "الإجراءات السريعة",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MidnightBlue,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(11.dp)
                ) {
                    // Button: Add Expense (💸)
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { onAddExpense() }
                            .border(1.dp, BorderGray, RoundedCornerShape(24.dp)),
                        color = PureWhite
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(LightRedContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("💸", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "إضافة مصروف",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = JetBlack
                            )
                        }
                    }

                    // Button: Add Shipment (📦)
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { onAddShipment() }
                            .border(1.dp, BorderGray, RoundedCornerShape(24.dp)),
                        color = PureWhite
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(LightGreenContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📦", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "إضافة شحنة",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = JetBlack
                            )
                        }
                    }
                }
            }
        }

        // Expense breakdown visualization
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                shape = RoundedCornerShape(28.dp),
                border = borderBrush()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "تحليل وتوزيع المصاريف 📊",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MidnightBlue
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (categoryExpenses.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "لم تقم بإدخال أي مصروف مالي لعرض التحليل بعد.",
                                fontSize = 12.sp,
                                color = GrayText,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        val maxVal = categoryExpenses.values.maxOrNull() ?: 1.0
                        categoryExpenses.forEach { (cat, amount) ->
                            val proportion = (amount / maxVal).toFloat()
                            val pct = if (totalExpenses > 0) (amount / totalExpenses) * 100 else 0.0

                            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = getCategoryIcon(cat),
                                            contentDescription = null,
                                            tint = ActiveBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = cat,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = JetBlack
                                        )
                                    }
                                    Text(
                                        text = "${String.format(Locale.US, "%,.1f", pct)}% (${amount.toInt()} ج.م)",
                                        fontSize = 12.sp,
                                        color = MidnightBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                // Customized Progress Bar
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(CircleShape)
                                        .background(SlateBackground)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(proportion)
                                            .height(8.dp)
                                            .clip(CircleShape)
                                            .background(
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        LightBlueContainer,
                                                        ActiveBlue
                                                    )
                                                )
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick overview history
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "آخر النشاطات الحالية 📋",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MidnightBlue
                    )
                    Text(
                        text = "عرض الكل",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ActiveBlue,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onNavigateToTab(1) } // Switch to cargo list
                            .padding(4.dp)
                    )
                }

                if (shipments.isEmpty() && expenses.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        border = borderBrush(),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "مستند نظيف! لا توجد مسودات حركات مالية مدخلة بعد.",
                                fontSize = 13.sp,
                                color = GrayText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    val combinedActivities = mutableListOf<ActivityItem>()
                    combinedActivities.addAll(shipments.map { ActivityItem.Shipment(it) })
                    combinedActivities.addAll(expenses.map { ActivityItem.Expense(it) })
                    val sortedActivities = combinedActivities.sortedByDescending { it.dateMillis }.take(5)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        border = borderBrush(),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            sortedActivities.forEachIndexed { idx, item ->
                                when (item) {
                                    is ActivityItem.Shipment -> {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(LightGreenContainer),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.LocalShipping,
                                                    contentDescription = "شحنة",
                                                    tint = DeepGreen,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "شحنة: ${item.data.customerName}",
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = JetBlack
                                                )
                                                Text(
                                                    text = SimpleDateFormat("yyyy/MM/dd", Locale("ar", "EG")).format(Date(item.data.dateMillis)),
                                                    fontSize = 11.sp,
                                                    color = GrayText
                                                )
                                            }
                                            Text(
                                                text = "+${item.data.amount} ج.م",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Black,
                                                color = DeepGreen
                                            )
                                        }
                                    }
                                    is ActivityItem.Expense -> {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(LightRedContainer),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = getCategoryIcon(item.data.category),
                                                    contentDescription = "مصروف",
                                                    tint = DeepRed,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "صرف: ${item.data.category}",
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = JetBlack
                                                )
                                                Text(
                                                    text = SimpleDateFormat("yyyy/MM/dd", Locale("ar", "EG")).format(Date(item.data.dateMillis)),
                                                    fontSize = 11.sp,
                                                    color = GrayText
                                                )
                                            }
                                            Text(
                                                text = "-${item.data.amount} ج.م",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Black,
                                                color = DeepRed
                                            )
                                        }
                                    }
                                }
                                if (idx < sortedActivities.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(BorderGray)
                                            .padding(horizontal = 12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShipmentsScreen(viewModel: TransportViewModel) {
    val shipments by viewModel.shipments.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    var editShipmentTarget by remember { mutableStateOf<ShipmentEntity?>(null) }
    var viewDetailsTarget by remember { mutableStateOf<ShipmentEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "إشغالات الشحنات والإيرادات 🚚",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (shipments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.LocalShipping,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "لا توجد شحنات مسجلة حالياً.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "اضغط على زر (إضافة شحنة) لتسجيل حركة النقل الأولى.",
                        fontSize = 12.sp,
                        color = Color.Gray.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(shipments) { shipment ->
                    // Calculate expenses tied to this specific shipment
                    val tiedExpenses = expenses.filter { it.shipmentId == shipment.id }
                    val totalTiedCost = tiedExpenses.sumOf { it.amount }
                    val netTripIncome = shipment.amount - totalTiedCost

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewDetailsTarget = shipment }
                            .testTag("shipment_card_${shipment.id}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = borderBrush()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = shipment.customerName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.DateRange,
                                            contentDescription = null,
                                            tint = Color.Gray,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = SimpleDateFormat("yyyy/MM/dd", Locale("ar", "EG")).format(Date(shipment.dateMillis)),
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Text(
                                    text = "${shipment.amount} ج.م",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF2E7D32)
                                )
                            }

                            // Show route if specified
                            if (shipment.origin.isNotEmpty() || shipment.destination.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                                        contentDescription = "الخط",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "المسار: ${shipment.origin.ifEmpty { "غير محدد" }} ⬅️ ${shipment.destination.ifEmpty { "غير محدد" }}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Display financial summary tag of the Trip
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "المصاريف: ${totalTiedCost.toInt()} ج.م",
                                        fontSize = 11.sp,
                                        color = if (totalTiedCost > 0) Color(0xFFC62828) else Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "الصافي: ${netTripIncome.toInt()} ج.م",
                                        fontSize = 11.sp,
                                        color = if (netTripIncome >= 0) Color(0xFF2E7D32) else Color(0xFFC62828),
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    IconButton(
                                        onClick = { editShipmentTarget = shipment },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Filled.Edit, contentDescription = "تعديل", modifier = Modifier.size(16.dp))
                                    }
                                    IconButton(
                                        onClick = { viewModel.deleteShipment(shipment) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "حذف",
                                            tint = Color(0xFFC62828),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Edit shipment target
        if (editShipmentTarget != null) {
            val ship = editShipmentTarget!!
            EditShipmentDialog(
                shipment = ship,
                onDismiss = { editShipmentTarget = null },
                onConfirm = { name, amt, date, origin, dest, notes ->
                    viewModel.updateShipment(ship.copy(customerName = name, amount = amt, dateMillis = date, origin = origin, destination = dest, notes = notes))
                    editShipmentTarget = null
                }
            )
        }

        // View shipment details with tied expenses
        if (viewDetailsTarget != null) {
            val ship = viewDetailsTarget!!
            val tiedExpenses = expenses.filter { it.shipmentId == ship.id }
            ShipmentDetailDialog(
                shipment = ship,
                tiedExpenses = tiedExpenses,
                onDismiss = { viewDetailsTarget = null }
            )
        }
    }
}

@Composable
fun ExpensesScreen(viewModel: TransportViewModel) {
    val expenses by viewModel.expenses.collectAsState()
    val shipments by viewModel.shipments.collectAsState()
    var editExpenseTarget by remember { mutableStateOf<ExpenseEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "دفتر المصروفات اليومية والتشغيل 💸",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (expenses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.MoneyOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "لم تقم بتسجيل أي مصروف مالي.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "استخدم زر (إضافة مصروف) لتسجيل الوقود، الصيانة، المرتبات أو الكارتات.",
                        fontSize = 11.sp,
                        color = Color.Gray.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(expenses) { expense ->
                    val linkedShipment = shipments.find { it.id == expense.shipmentId }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("expense_card_${expense.id}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = borderBrush()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = getCategoryIcon(expense.category),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = expense.category,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Filled.DateRange,
                                                contentDescription = null,
                                                tint = Color.Gray,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = SimpleDateFormat("yyyy/MM/dd", Locale("ar", "EG")).format(Date(expense.dateMillis)),
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = "${expense.amount} ج.م",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFC62828)
                                )
                            }

                            // Show linked trip tag
                            if (linkedShipment != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(Color(0xFFE8F5E9), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.LocalShipping,
                                        contentDescription = null,
                                        tint = Color(0xFF2E7D32),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "مرتبط برحلة العميل: ${linkedShipment.customerName}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                            }

                            // Notes
                            if (expense.notes.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Notes, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = expense.notes,
                                        fontSize = 11.sp,
                                        color = Color.DarkGray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(
                                    onClick = { editExpenseTarget = expense },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Filled.Edit, contentDescription = "تعديل", modifier = Modifier.size(16.dp))
                                }
                                IconButton(
                                    onClick = { viewModel.deleteExpense(expense) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "حذف",
                                        tint = Color(0xFFC62828),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Edit Expense handler
        if (editExpenseTarget != null) {
            val exp = editExpenseTarget!!
            EditExpenseDialog(
                expense = exp,
                shipmentsList = shipments,
                onDismiss = { editExpenseTarget = null },
                onConfirm = { amt, cat, date, notes, boundShipId ->
                    viewModel.updateExpense(exp.copy(amount = amt, category = cat, dateMillis = date, notes = notes, shipmentId = boundShipId))
                    editExpenseTarget = null
                }
            )
        }
    }
}

@Composable
fun ReportsScreen(viewModel: TransportViewModel) {
    val context = LocalContext.current
    val shipments by viewModel.shipments.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val netProfit by viewModel.netProfit.collectAsState()

    var filterInterval by remember { mutableIntStateOf(0) } // 0: الكل, 1: هذا الشهر, 2: الأسبوع الحالي

    // Filter calculations
    val now = Calendar.getInstance()
    val startOfMonthMillis = remember {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.timeInMillis
    }
    val startOfWeekMillis = remember {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.timeInMillis
    }

    val filteredShipments = shipments.filter {
        when (filterInterval) {
            1 -> it.dateMillis >= startOfMonthMillis
            2 -> it.dateMillis >= startOfWeekMillis
            else -> true
        }
    }

    val filteredExpenses = expenses.filter {
        when (filterInterval) {
            1 -> it.dateMillis >= startOfMonthMillis
            2 -> it.dateMillis >= startOfWeekMillis
            else -> true
        }
    }

    val displayRevenue = filteredShipments.sumOf { it.amount }
    val displayExpenses = filteredExpenses.sumOf { it.amount }
    val displayNet = displayRevenue - displayExpenses

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "التقارير والمشاركة والمحاسبة 📈",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "فرز الحركات المالية ومشاركتها مع المالك أو العميل فوراً.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }

        // Interval selector Row
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = borderBrush()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "تعديل النطاق الزمني للصفحة", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val intervals = listOf("كل البيانات", "هذا الشهر", "هذا الأسبوع")
                        intervals.forEachIndexed { idx, txt ->
                            val selected = filterInterval == idx
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { filterInterval = idx }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = txt,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Statistics Display Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                border = borderBrush()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "الملخص المالي الفعلي للنطاق المختار",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("الإيرادات المكتسبة", fontSize = 11.sp, color = Color.Gray)
                            Text("${displayRevenue} ج.م", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF2E7D32))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("تكاليف التشغيل", fontSize = 11.sp, color = Color.Gray)
                            Text("${displayExpenses} ج.م", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFC62828))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("صافي الأرباح", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                text = "${displayNet} ج.م",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = if (displayNet >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                        }
                    }
                }
            }
        }

        // Action Share Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = borderBrush()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "خيارات الإرسال والتصدير",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Export CSV excel button
                    Button(
                        onClick = { viewModel.shareLedgerAsCSV(context, "all") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.FileDownload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تصدير الحسابات الشاملة (CSV / Excel)", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Social share WhatsApp button
                    Button(
                        onClick = { viewModel.shareFormattedTextReport(context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)), // WhatsApp Color!
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("إرسال تقرير منسق (WhatsApp / SMS)", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "💡 نصيحة: يمكنك النقر على زر تصدير Excel لمشاركة ملف الحسابات بالكامل لفتحه في الحاسوب وتعديله في أي وقت.",
                        fontSize = 10.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Section header for filtered listing
        item {
            Text(
                text = "سجل الإدخال في النطاق الحالي (${filteredShipments.size + filteredExpenses.size} حركات)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // List showing filtered movements
        if (filteredShipments.isEmpty() && filteredExpenses.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("لا توجد حركة في هذا النطاق المالي.", fontSize = 12.sp, color = Color.Gray)
                }
            }
        } else {
            val combinedList = mutableListOf<ActivityItem>()
            combinedList.addAll(filteredShipments.map { ActivityItem.Shipment(it) })
            combinedList.addAll(filteredExpenses.map { ActivityItem.Expense(it) })
            val sortedList = combinedList.sortedByDescending { it.dateMillis }

            items(sortedList) { el ->
                val isShipment = el is ActivityItem.Shipment
                val titleText = when (el) {
                    is ActivityItem.Shipment -> "شحنة: ${el.data.customerName}"
                    is ActivityItem.Expense -> "صرف: ${el.data.category}"
                }
                val valueText = when (el) {
                    is ActivityItem.Shipment -> "+${el.data.amount} ج.م"
                    is ActivityItem.Expense -> "-${el.data.amount} ج.م"
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isShipment) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isShipment) Icons.Filled.LocalShipping else Icons.Filled.ReceiptLong,
                                contentDescription = null,
                                tint = if (isShipment) Color(0xFF2E7D32) else Color(0xFFC62828),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = titleText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = SimpleDateFormat("yyyy/MM/dd", Locale("ar", "EG")).format(Date(el.dateMillis)),
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Text(
                        text = valueText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isShipment) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }
            }
        }
    }
}

// Activity Item wrapper sealed class
sealed class ActivityItem {
    abstract val dateMillis: Long

    data class Shipment(val data: ShipmentEntity) : ActivityItem() {
        override val dateMillis: Long get() = data.dateMillis
    }

    data class Expense(val data: ExpenseEntity) : ActivityItem() {
        override val dateMillis: Long get() = data.dateMillis
    }
}

@Composable
fun AddShipmentDialog(onDismiss: () -> Unit, onConfirm: (name: String, amt: Double, date: Long, origin: String, dest: String, notes: String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var origin by remember { mutableStateOf("") }
    var dest by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf(Date()) }
    val calendar = Calendar.getInstance()
    val context = LocalContext.current

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.time
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "تسجيل شحنة إيرادات جديدة 🚚",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسم العميل *") },
                    colors = textFieldsThemeCols(),
                    modifier = Modifier.fillMaxWidth().testTag("shipment_customer_input")
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("قيمة الشحنة (ج.م) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldsThemeCols(),
                    modifier = Modifier.fillMaxWidth().testTag("shipment_amount_input")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = origin,
                        onValueChange = { origin = it },
                        label = { Text("المصدر (من)") },
                        modifier = Modifier.weight(1f),
                        colors = textFieldsThemeCols()
                    )
                    OutlinedTextField(
                        value = dest,
                        onValueChange = { dest = it },
                        label = { Text("الوجهة (إلى)") },
                        modifier = Modifier.weight(1f),
                        colors = textFieldsThemeCols()
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                        .clickable { datePickerDialog.show() }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("تاريخ الشحنة:", fontSize = 13.sp)
                    Text(
                        text = SimpleDateFormat("yyyy / MM / dd", Locale("ar", "EG")).format(selectedDate),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldsThemeCols()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val amt = amountText.toDoubleOrNull()
                            if (name.isEmpty()) {
                                Toast.makeText(context, "الرجاء إدخال اسم العميل", Toast.LENGTH_SHORT).show()
                            } else if (amt == null || amt <= 0.0) {
                                Toast.makeText(context, "الرجاء إدخال مبلغ صحيح للشحنة", Toast.LENGTH_SHORT).show()
                            } else {
                                onConfirm(name, amt, selectedDate.time, origin, dest, notes)
                            }
                        },
                        modifier = Modifier.weight(1.5f).height(48.dp).testTag("shipment_submit_btn")
                    ) {
                        Text("حفظ", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("إلغاء")
                    }
                }
            }
        }
    }
}

@Composable
fun EditShipmentDialog(shipment: ShipmentEntity, onDismiss: () -> Unit, onConfirm: (name: String, amt: Double, date: Long, origin: String, dest: String, notes: String) -> Unit) {
    var name by remember { mutableStateOf(shipment.customerName) }
    var amountText by remember { mutableStateOf(shipment.amount.toString()) }
    var origin by remember { mutableStateOf(shipment.origin) }
    var dest by remember { mutableStateOf(shipment.destination) }
    var notes by remember { mutableStateOf(shipment.notes) }

    var selectedDate by remember { mutableStateOf(Date(shipment.dateMillis)) }
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate
    val context = LocalContext.current

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.time
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "تعديل الشحنة 🛠️",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسم العميل *") },
                    colors = textFieldsThemeCols(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("قيمة الشحنة (ج.م) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldsThemeCols(),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = origin,
                        onValueChange = { origin = it },
                        label = { Text("المصدر") },
                        modifier = Modifier.weight(1f),
                        colors = textFieldsThemeCols()
                    )
                    OutlinedTextField(
                        value = dest,
                        onValueChange = { dest = it },
                        label = { Text("الوجهة") },
                        modifier = Modifier.weight(1f),
                        colors = textFieldsThemeCols()
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                        .clickable { datePickerDialog.show() }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("تاريخ الشحنة:", fontSize = 13.sp)
                    Text(
                        text = SimpleDateFormat("yyyy / MM / dd", Locale("ar", "EG")).format(selectedDate),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldsThemeCols()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val amt = amountText.toDoubleOrNull()
                            if (name.isEmpty()) {
                                Toast.makeText(context, "الرجاء إدخال اسم العميل", Toast.LENGTH_SHORT).show()
                            } else if (amt == null || amt <= 0.0) {
                                Toast.makeText(context, "الرجاء إدخال مبلغ صحيح", Toast.LENGTH_SHORT).show()
                            } else {
                                onConfirm(name, amt, selectedDate.time, origin, dest, notes)
                            }
                        },
                        modifier = Modifier.weight(1.5f).height(48.dp)
                    ) {
                        Text("حفظ التغييرات", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("إلغاء")
                    }
                }
            }
        }
    }
}

@Composable
fun AddExpenseDialog(shipmentsList: List<ShipmentEntity>, onDismiss: () -> Unit, onConfirm: (amt: Double, cat: String, date: Long, notes: String, boundShipId: Int?) -> Unit) {
    var amountText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ARABIC_EXPENSE_CATEGORIES[0]) }
    var notes by remember { mutableStateOf("") }
    var boundShipmentId by remember { mutableStateOf<Int?>(null) }

    var selectedDate by remember { mutableStateOf(Date()) }
    val calendar = Calendar.getInstance()
    val context = LocalContext.current

    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showShipmentDropdown by remember { mutableStateOf(false) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.time
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "تسجيل مصروف يومي أو تشغيلي 💸",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("المبلغ المالي (ج.م) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldsThemeCols(),
                    modifier = Modifier.fillMaxWidth().testTag("expense_amount_input")
                )

                // Category selection dropdown Custom Composable
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "فئة المصروف:", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                            .clickable { showCategoryDropdown = true }
                            .padding(14.dp)
                            .testTag("expense_category_selector")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = selectedCategory, fontWeight = FontWeight.Bold)
                            Icon(Icons.Filled.ReceiptLong, contentDescription = null, modifier = Modifier.size(20.dp))
                        }

                        DropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            ARABIC_EXPENSE_CATEGORIES.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        selectedCategory = category
                                        showCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Linking expense to an active cargo shipment
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "ربط المصروف برحلة معينة (اختياري):", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                            .clickable { showShipmentDropdown = true }
                            .padding(14.dp)
                    ) {
                        val activeBound = shipmentsList.find { it.id == boundShipmentId }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (activeBound != null) "شحنة: ${activeBound.customerName} (${activeBound.amount} ج.م)" else "عام (مصروف غير مرتبط بشحنة معينة)",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Icon(Icons.Filled.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp))
                        }

                        DropdownMenu(
                            expanded = showShipmentDropdown,
                            onDismissRequest = { showShipmentDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            DropdownMenuItem(
                                text = { Text("عام (غير مرتبط برحلة)") },
                                onClick = {
                                    boundShipmentId = null
                                    showShipmentDropdown = false
                                }
                            )
                            shipmentsList.forEach { shipment ->
                                DropdownMenuItem(
                                    text = { Text("${shipment.customerName} (${shipment.amount.toInt()} ج.م)") },
                                    onClick = {
                                        boundShipmentId = shipment.id
                                        showShipmentDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                        .clickable { datePickerDialog.show() }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("تاريخ الصرف:", fontSize = 13.sp)
                    Text(
                        text = SimpleDateFormat("yyyy / MM / dd", Locale("ar", "EG")).format(selectedDate),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("بيانات وتفاصيل أخرى") },
                    colors = textFieldsThemeCols(),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val amt = amountText.toDoubleOrNull()
                            if (amt == null || amt <= 0.0) {
                                Toast.makeText(context, "الرجاء إدخال مبلغ صحيح للصرف", Toast.LENGTH_SHORT).show()
                            } else {
                                onConfirm(amt, selectedCategory, selectedDate.time, notes, boundShipmentId)
                            }
                        },
                        modifier = Modifier.weight(1.5f).height(48.dp).testTag("expense_submit_btn")
                    ) {
                        Text("حفظ المصروف", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("إلغاء")
                    }
                }
            }
        }
    }
}

@Composable
fun EditExpenseDialog(expense: ExpenseEntity, shipmentsList: List<ShipmentEntity>, onDismiss: () -> Unit, onConfirm: (amt: Double, cat: String, date: Long, notes: String, boundShipId: Int?) -> Unit) {
    var amountText by remember { mutableStateOf(expense.amount.toString()) }
    var selectedCategory by remember { mutableStateOf(expense.category) }
    var notes by remember { mutableStateOf(expense.notes) }
    var boundShipmentId by remember { mutableStateOf(expense.shipmentId) }

    var selectedDate by remember { mutableStateOf(Date(expense.dateMillis)) }
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate
    val context = LocalContext.current

    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showShipmentDropdown by remember { mutableStateOf(false) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.time
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "تعديل المصروف 🛠️",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("المبلغ المالي (ج.م) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldsThemeCols(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Category selection dropdown Custom Composable
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "فئة المصروف:", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                            .clickable { showCategoryDropdown = true }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = selectedCategory, fontWeight = FontWeight.Bold)
                            Icon(Icons.Filled.ReceiptLong, contentDescription = null, modifier = Modifier.size(20.dp))
                        }

                        DropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            ARABIC_EXPENSE_CATEGORIES.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        selectedCategory = category
                                        showCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Linking expense to an active cargo shipment
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "ربط المصروف برحلة معينة (اختياري):", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                            .clickable { showShipmentDropdown = true }
                            .padding(14.dp)
                    ) {
                        val activeBound = shipmentsList.find { it.id == boundShipmentId }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (activeBound != null) "شحنة: ${activeBound.customerName} (${activeBound.amount} ج.م)" else "عام (مصروف غير مرتبط بشحنة معينة)",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Icon(Icons.Filled.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp))
                        }

                        DropdownMenu(
                            expanded = showShipmentDropdown,
                            onDismissRequest = { showShipmentDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            DropdownMenuItem(
                                text = { Text("عام (غير مرتبط برحلة)") },
                                onClick = {
                                    boundShipmentId = null
                                    showShipmentDropdown = false
                                }
                            )
                            shipmentsList.forEach { shipment ->
                                DropdownMenuItem(
                                    text = { Text("${shipment.customerName} (${shipment.amount.toInt()} ج.م)") },
                                    onClick = {
                                        boundShipmentId = shipment.id
                                        showShipmentDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                        .clickable { datePickerDialog.show() }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("تاريخ الصرف:", fontSize = 13.sp)
                    Text(
                        text = SimpleDateFormat("yyyy / MM / dd", Locale("ar", "EG")).format(selectedDate),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("بيانات وتفاصيل أخرى") },
                    colors = textFieldsThemeCols(),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val amt = amountText.toDoubleOrNull()
                            if (amt == null || amt <= 0.0) {
                                Toast.makeText(context, "الرجاء إدخال مبلغ صحيح للصرف", Toast.LENGTH_SHORT).show()
                            } else {
                                onConfirm(amt, selectedCategory, selectedDate.time, notes, boundShipmentId)
                            }
                        },
                        modifier = Modifier.weight(1.5f).height(48.dp)
                    ) {
                        Text("حفظ التعديل", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("إلغاء")
                    }
                }
            }
        }
    }
}

@Composable
fun ShipmentDetailDialog(shipment: ShipmentEntity, tiedExpenses: List<ExpenseEntity>, onDismiss: () -> Unit) {
    val totalExpense = tiedExpenses.sumOf { it.amount }
    val netRevenue = shipment.amount - totalExpense

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "تفاصيل رحلة شحن: ${shipment.customerName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Date and route indicator
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("تاريخ الرحلة:", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = SimpleDateFormat("yyyy/MM/dd", Locale("ar", "EG")).format(Date(shipment.dateMillis)),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (shipment.origin.isNotEmpty() || shipment.destination.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("خط السير والموقع:", fontSize = 12.sp, color = Color.Gray)
                                Text(
                                    text = "${shipment.origin} ➡️ ${shipment.destination}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Balance summary
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("إجمالي الإيراد المقبوض:", fontSize = 13.sp)
                        Text("${shipment.amount} ج.م", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("مجموع مصاريف الرحلة:", fontSize = 13.sp)
                        Text("${totalExpense} ج.م", fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("صافي الفائدة الفعلي للرحلة:", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${netRevenue} ج.م",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = if (netRevenue >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }

                // Listing tied expenses
                Text(
                    text = "المصاريف التفصيلية لهذه الشحنة (${tiedExpenses.size})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (tiedExpenses.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("لا توجد مصاريف خاصة مرتبطة بهذه الشحنة.", fontSize = 11.sp, color = Color.Gray)
                    }
                } else {
                    Box(modifier = Modifier.height(100.dp)) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(tiedExpenses) { expense ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Gray.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = getCategoryIcon(expense.category),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(expense.category, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    }

                                    Text("-${expense.amount} ج.م", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                                }
                            }
                        }
                    }
                }

                if (shipment.notes.isNotEmpty()) {
                    Column {
                        Text("ملاحظات الشحنة:", fontSize = 12.sp, color = Color.Gray)
                        Text(shipment.notes, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Text("إغلاق النافذة")
                }
            }
        }
    }
}

@Composable
fun borderBrush() = androidx.compose.foundation.BorderStroke(
    width = 1.dp,
    color = BorderGray
)

@Composable
fun textFieldsThemeCols() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MidnightBlue,
    unfocusedBorderColor = BorderGray,
    focusedLabelColor = MidnightBlue,
    unfocusedLabelColor = GrayText
)
