package com.example.spendoov2

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect // <-- TAMBAHKAN
import com.google.firebase.auth.FirebaseAuth // <-- TAMBAHKAN
import com.google.firebase.firestore.ktx.firestore // <-- TAMBAHKAN
import com.google.firebase.ktx.Firebase // <-- TAMBAHKAN
import android.util.Log // <-- TAMBAHKAN
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendoov2.ui.theme.MainBackgroundColor
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.core.graphics.withRotation
import java.time.Month


private data class TooltipData(
    val day: Int,
    val currentAmount: Int,
    val previousAmount: Int,
    val tapPosition: Offset,
    val barTopPosition: Float
)

// A predefined list of colors for the charts
val chartColors = listOf(
    Color(0xFFB0FFDD), Color(0xFF9CFFC4), Color(0xFF7AB6FF), Color(0xFFFFBEBE),
    Color(0xFF7DFFFF), Color(0xFF54E871), Color(0xFFFFC107), Color(0xFFFFA8A8),
    Color(0xFFFF77FF), Color(0xFFBAFFC2), Color(0xFFFFFF70), Color(0xFFFFA4A4)
)

// Helper function to format numbers as currency
fun formatCurrency(amount: Int): String {
    return "Rp${"%,d".format(amount)}"
}

// ===============================
// 🔹 MAIN SCREEN
// ===============================
@Composable
fun AnalyzeAndAdviceScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Statistics") }
    var selectedPeriod by remember { mutableStateOf("Monthly") }
    var selectedYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedYear by remember { mutableStateOf(Year.now()) }

    // --- TAMBAHKAN LOGIKA PENGAMBILAN DATA ---
    var allTransactions by remember { mutableStateOf(emptyList<TransactionData>()) }
    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore
    val currentUser = auth.currentUser

    LaunchedEffect(currentUser) { // Hanya perlu dijalankan sekali saat user berubah
        if (currentUser != null) {
            // Mode Login: Ambil data dari Firestore
            db.collection("users")
                .document(currentUser.uid)
                .collection("transactions")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("Firestore", "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val firestoreTransactions = snapshot.documents.map { doc ->
                            doc.toObject(TransactionData::class.java)?.copy(id = doc.id)
                        }
                        allTransactions = firestoreTransactions.filterNotNull()
                    }
                }
        } else {
            // Mode Guest: Ambil data dari List lokal
            allTransactions = TransactionLists.toList()
        }
    }

    // This will reactively filter transactions whenever the selected period changes
    val (transactionsForPeriod, previousPeriodTransactions) = remember(selectedPeriod, selectedYearMonth, selectedYear, allTransactions) {
        val current = when (selectedPeriod) {
            "Monthly" -> allTransactions.filter {
                it.year == selectedYearMonth.year && it.month.equals(selectedYearMonth.month.name, ignoreCase = true)
            }
            "Yearly" -> allTransactions.filter { it.year == selectedYear.value }
            else -> emptyList()
        }

        val previous = when (selectedPeriod) {
            "Monthly" -> {
                val prevMonth = selectedYearMonth.minusMonths(1)
                allTransactions.filter {
                    it.year == prevMonth.year && it.month.equals(prevMonth.month.name, ignoreCase = true)
                }
            }
            "Yearly" -> {
                val prevYear = selectedYear.minusYears(1)
                allTransactions.filter { it.year == prevYear.value }
            }
            else -> emptyList()
        }
        Pair(current, previous)
    }

    // Calculate all metrics from the filtered data
    val totalIncome = transactionsForPeriod.filter { it.type == "income" }.sumOf { it.amount }
    val totalExpense = transactionsForPeriod.filter { it.type == "expense" }.sumOf { it.amount }

    val incomeByCategory = transactionsForPeriod.filter { it.type == "income" }
        .groupBy { it.category }
        .mapValues { (_, tx) -> tx.sumOf { it.amount } }
        .toList().sortedByDescending { it.second }.toMap() // Sort for consistent color assignment

    val expenseByCategory = transactionsForPeriod.filter { it.type == "expense" }
        .groupBy { it.category }
        .mapValues { (_, tx) -> tx.sumOf { it.amount } }
        .toList().sortedByDescending { it.second }.toMap()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MainBackgroundColor),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            AnalyzeHeader(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                selectedPeriod = selectedPeriod,
                onPeriodSelected = { selectedPeriod = it },
                selectedYearMonth = selectedYearMonth,
                selectedYear = selectedYear,
                onDateChange = { delta ->
                    if (selectedPeriod == "Monthly") {
                        selectedYearMonth = selectedYearMonth.plusMonths(delta.toLong())
                    } else {
                        selectedYear = selectedYear.plusYears(delta.toLong())
                    }
                },
                navController = navController
            )
        }

        if (selectedTab == "Advice") {
            item {
                FinancialSummary(
                    period = selectedPeriod,
                    currentTransactions = transactionsForPeriod,
                    previousTransactions = previousPeriodTransactions,
                    incomeByCategory = incomeByCategory,
                    expenseByCategory = expenseByCategory
                )
            }

            if (expenseByCategory.isNotEmpty()) {
                item {
                    Text(
                        text = "Your Spending Breakdown",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp)
                    )
                    Divider(
                        color = Color(0xFF1AFF9C),
                        thickness = 2.dp,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                    )
                }
                items(expenseByCategory.toList()) { (category, amount) ->
                    CategoryDetailCard(
                        categoryName = category,
                        amount = amount,
                        percentageOfTotal = if (totalExpense > 0) (amount.toDouble() / totalExpense * 100).toFloat() else 0f,
                        color = chartColors[expenseByCategory.keys.indexOf(category) % chartColors.size]
                    )
                }
            }

        } else { // Statistics Tab
            item {
                StatisticsScreen(
                    selectedPeriod = selectedPeriod,
                    totalIncome = totalIncome,
                    totalExpense = totalExpense,
                    previousTransactions = previousPeriodTransactions,
                    transactionsForPeriod = transactionsForPeriod,
                    selectedYearMonth = selectedYearMonth,
                    selectedYear = selectedYear // Add this line
                )
            }
        }
    }
}

// ===============================
// 🔹 HEADER SECTION (Updated)
// ===============================
@Composable
fun AnalyzeHeader(
    selectedTab: String, onTabSelected: (String) -> Unit,
    selectedPeriod: String, onPeriodSelected: (String) -> Unit,
    selectedYearMonth: YearMonth, selectedYear: Year,
    onDateChange: (Int) -> Unit,
    navController: NavController
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(painter = painterResource(id = R.drawable.arrow_side_line_left), "Back", tint = Color.White)
            }
            Text("SPENDOO", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { /* TODO */ }) {
                Icon(painter = painterResource(id = R.drawable.dot_option), "Options", tint = Color.White)
            }
        }
        Spacer(Modifier.height(15.dp))

        // Tabs
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("Statistics", "Advice").forEach { tab ->
                val isSelected = selectedTab == tab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f).clickable { onTabSelected(tab) }
                ) {
                    Text(text = tab, color = if (isSelected) Color.White else Color(0xFFB8FFCD), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 16.sp)
                    Box(Modifier.height(3.dp).fillMaxWidth().background(if (isSelected) Brush.horizontalGradient(listOf(Color(0xFF004D26), Color(0xFF00FF80), Color(0xFF004D26))) else Brush.horizontalGradient(listOf(Color(0xFF003320), Color(0xFF003D25)))))
                }
            }
        }
        Spacer(Modifier.height(25.dp))

        // Period Selector
        val periodOptions = if (selectedTab == "Statistics") listOf("Monthly", "Yearly") else listOf("Monthly", "Yearly")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            periodOptions.forEach { period ->
                val isSelected = selectedPeriod == period
                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .background(if (isSelected) Brush.horizontalGradient(listOf(Color(0xFF54E871), Color(0xFF2F9944))) else Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent)), shape = RoundedCornerShape(12.dp))
                        .clickable { onPeriodSelected(period) }
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Text(period, color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 15.sp)
                }
            }
        }

        // --- THIS IS THE CHANGE ---
        // The 'if' condition has been removed to show the date navigation on all tabs.
        Spacer(Modifier.height(25.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.arrow_side_line_left), "Previous", tint = Color.White, modifier = Modifier.size(24.dp).clickable { onDateChange(-1) })
            Text(
                text = if (selectedPeriod == "Monthly") selectedYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US)) else selectedYear.toString(),
                color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
            Icon(painter = painterResource(id = R.drawable.arrow_side_line_right), "Next", tint = Color.White, modifier = Modifier.size(24.dp).clickable { onDateChange(1) })
        }
        Spacer(Modifier.height(25.dp))
    }
}

// ===============================
// 🔹 FINANCIAL SUMMARY (Replaces Income/Expense stats)
// ===============================
@Composable
fun FinancialSummary(
    period: String,
    currentTransactions: List<TransactionData>,
    previousTransactions: List<TransactionData>,
    incomeByCategory: Map<String, Int>,
    expenseByCategory: Map<String, Int>
) {
    val totalIncome = currentTransactions.filter { it.type == "income" }.sumOf { it.amount }
    val totalExpense = currentTransactions.filter { it.type == "expense" }.sumOf { it.amount }
    val prevTotalIncome = previousTransactions.filter { it.type == "income" }.sumOf { it.amount }
    val prevTotalExpense = previousTransactions.filter { it.type == "expense" }.sumOf { it.amount }

    fun calculatePercentageChange(current: Int, previous: Int): Pair<Float, Boolean> {
        if (previous == 0) return Pair(0f, true) // No change or infinite growth
        val change = ((current.toDouble() - previous) / previous * 100)
        return Pair(change.toFloat(), change >= 0)
    }

    val (incomeChange, isIncomeIncrease) = calculatePercentageChange(totalIncome, prevTotalIncome)
    val (expenseChange, isExpenseIncrease) = calculatePercentageChange(totalExpense, prevTotalExpense)

    val periodName = if (period == "Monthly") "Month" else "Year"

    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(16.dp))

        // --- Income Section ---
        SummarySection(
            title = "Income This $periodName",
            totalAmount = totalIncome,
            changePercentage = incomeChange,
            isIncrease = isIncomeIncrease,
            comparisonPeriod = period,
            categoryData = incomeByCategory,
            isIncome = true
        )

        Spacer(Modifier.height(30.dp))

        // --- Expense Section ---
        SummarySection(
            title = "Expense This $periodName",
            totalAmount = totalExpense,
            changePercentage = expenseChange,
            isIncrease = isExpenseIncrease,
            comparisonPeriod = period,
            categoryData = expenseByCategory,
            isIncome = false
        )
    }
}

@Composable
fun SummarySection(
    title: String,
    totalAmount: Int,
    changePercentage: Float,
    isIncrease: Boolean,
    comparisonPeriod: String,
    categoryData: Map<String, Int>,
    isIncome: Boolean
) {
    val accentColor = if (isIncome) Color(0xFF2F9944) else Color(0xFFFF2626)
    val changeColor = if (isIncrease == isIncome) Color(0xFF35FF4D) else Color(0xFFFF5E5E)
    val arrow = if (isIncrease) "▲" else "▼"
    val changeText = if (isIncrease) "increased" else "decreased"
    val comparisonText = if (comparisonPeriod == "Monthly") "last month" else "last year"

    Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    Divider(color = Color(0xFF003320), thickness = 2.dp, modifier = Modifier.padding(top = 4.dp, bottom = 14.dp))

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text("TOTAL:", color = accentColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(6.dp))
            Text(formatCurrency(totalAmount), color = Color(0xFFEDFFF5), fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(arrow, color = changeColor, fontSize = 16.sp, modifier = Modifier.padding(end = 4.dp))
            Text(
                "Your total $changeText by ${"%.1f".format(changePercentage.coerceIn(-999f, 999f))}% compared to $comparisonText.",
                color = changeColor, fontSize = 13.sp, textAlign = TextAlign.Center
            )
        }
    }
    Spacer(Modifier.height(10.dp))

    if (categoryData.isNotEmpty()) {
        Row(
            Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DonutChart(data = categoryData)
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 30.dp)
            ) {
                categoryData.keys.forEachIndexed { index, category ->
                    LegendItem(color = chartColors[index % chartColors.size], label = category)
                }
            }
        }
    } else {
        Text("No data for this period.", color = Color.Gray, modifier = Modifier.padding(vertical = 24.dp).fillMaxWidth(), textAlign = TextAlign.Center)
    }
}


// ===============================
// 🔹 DYNAMIC CATEGORY CARD (Replaces AdviceCard)
// ===============================
@Composable
fun CategoryDetailCard(categoryName: String, amount: Int, percentageOfTotal: Float, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.8f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp, horizontal = 16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(categoryName, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(formatCurrency(amount), color = Color.Black.copy(alpha = 0.9f), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "This is ${"%.1f".format(percentageOfTotal)}% of your total expense for this period.",
                color = Color.Black.copy(alpha = 0.8f), fontSize = 13.sp, lineHeight = 18.sp
            )
        }
    }
}

// ===============================
// 🔹 DYNAMIC CHART & LEGEND
// ===============================
@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).background(color, shape = CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
    }
}

@Composable
fun DonutChart(data: Map<String, Int>) {
    val total = data.values.sum().toFloat()
    if (total == 0f) return // Avoid drawing an empty chart

    val angles = data.map { (it.value / total) * 360f }
    val colors = data.keys.mapIndexed { index, _ -> chartColors[index % chartColors.size] }
    var startAngle = -90f

    Box(Modifier.size(150.dp).padding(8.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.matchParentSize()) {
            angles.forEachIndexed { index, sweepAngle ->
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle - 2f, // Small gap between slices
                    useCenter = false,
                    style = Stroke(width = 40f)
                )
                startAngle += sweepAngle
            }
        }
    }
}

// ===============================
// 🔹 STATISTICS SCREEN (Updated)
// ===============================
@Composable
fun StatisticsScreen(
    selectedPeriod: String,
    totalIncome: Int,
    totalExpense: Int,
    previousTransactions: List<TransactionData>,
    transactionsForPeriod: List<TransactionData>,
    selectedYearMonth: YearMonth,
    selectedYear: Year // Add selectedYear
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (selectedPeriod == "Monthly") {
            // --- Monthly View ---
            val currentDailyIncome = transactionsForPeriod.filter { it.type == "income" }.groupBy { it.date }.mapValues { (_, tx) -> tx.sumOf { it.amount } }
            val prevDailyIncome = previousTransactions.filter { it.type == "income" }.groupBy { it.date }.mapValues { (_, tx) -> tx.sumOf { it.amount } }
            val currentDailyExpense = transactionsForPeriod.filter { it.type == "expense" }.groupBy { it.date }.mapValues { (_, tx) -> tx.sumOf { it.amount } }
            val prevDailyExpense = previousTransactions.filter { it.type == "expense" }.groupBy { it.date }.mapValues { (_, tx) -> tx.sumOf { it.amount } }

            DailyBarChart(
                title = "Daily Income", currentData = currentDailyIncome, previousData = prevDailyIncome,
                currentColor = Color(0xFF00FF80), previousColor = Color(0xFF006B36),
                yearMonth = selectedYearMonth, modifier = Modifier.fillMaxWidth().height(250.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            DailyBarChart(
                title = "Daily Expense", currentData = currentDailyExpense, previousData = prevDailyExpense,
                currentColor = Color(0xFFFF6060), previousColor = Color(0xFF8B2B2B),
                yearMonth = selectedYearMonth, modifier = Modifier.fillMaxWidth().height(250.dp)
            )

        } else {
            // --- Yearly View ---
            val monthOrder = Month.entries.map { it.name }

            val currentMonthlyIncome = transactionsForPeriod.filter { it.type == "income" }.groupBy { it.month.uppercase() }.mapValues { (_, tx) -> tx.sumOf { it.amount } }
            val prevMonthlyIncome = previousTransactions.filter { it.type == "income" }.groupBy { it.month.uppercase() }.mapValues { (_, tx) -> tx.sumOf { it.amount } }
            val currentMonthlyExpense = transactionsForPeriod.filter { it.type == "expense" }.groupBy { it.month.uppercase() }.mapValues { (_, tx) -> tx.sumOf { it.amount } }
            val prevMonthlyExpense = previousTransactions.filter { it.type == "expense" }.groupBy { it.month.uppercase() }.mapValues { (_, tx) -> tx.sumOf { it.amount } }

            MonthlyBarChart(
                title = "Monthly Income", currentData = currentMonthlyIncome, previousData = prevMonthlyIncome,
                currentColor = Color(0xFF00FF80), previousColor = Color(0xFF006B36),
                year = selectedYear, modifier = Modifier.fillMaxWidth().height(250.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            MonthlyBarChart(
                title = "Monthly Expense", currentData = currentMonthlyExpense, previousData = prevMonthlyExpense,
                currentColor = Color(0xFFFF6060), previousColor = Color(0xFF8B2B2B),
                year = selectedYear, modifier = Modifier.fillMaxWidth().height(250.dp)
            )
        }
    }
}


// ===============================
// 🔹 NEW MONTHLY BAR CHART
// ===============================
@OptIn(ExperimentalTextApi::class)
@Composable
fun MonthlyBarChart(
    title: String,
    currentData: Map<String, Int>, // Key is Month Name (e.g., "JANUARY")
    previousData: Map<String, Int>,
    currentColor: Color,
    previousColor: Color,
    year: Year,
    modifier: Modifier = Modifier
) {
    var tooltipData by remember { mutableStateOf<TooltipData?>(null) }
    val textMeasurer = rememberTextMeasurer()
    val scrollState = rememberScrollState()

    // Map month names to their integer value (1-12)
    val monthMap = Month.values().associate { it.name to it.value }
    val monthNames = Month.values().map { it.name.substring(0, 3).uppercase() } // e.g., "JAN", "FEB"

    Column(modifier = modifier) {
        val yAxisTitlePaint = remember { Paint().asFrameworkPaint().apply {
            isAntiAlias = true; textSize = 46f; color = android.graphics.Color.WHITE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }}

        Row(modifier = Modifier.fillMaxSize()) {
            // Y-Axis Title
            Canvas(modifier = Modifier.fillMaxHeight().width(40.dp)) {
                val nativeCanvas = drawContext.canvas.nativeCanvas; nativeCanvas.withRotation(-90f) {
                drawText(
                    title.uppercase(),
                    -(size.height / (1.45).toFloat()),
                    yAxisTitlePaint.textSize,
                    yAxisTitlePaint
                )
            }
            }

            // Horizontally Scrollable Chart
            Box(modifier = Modifier.fillMaxHeight().weight(1f).horizontalScroll(scrollState)) {
                val monthWidth = 64.dp // Wider bars for months
                val chartCanvasWidth = monthWidth * 12

                Canvas(modifier = Modifier.width(chartCanvasWidth).fillMaxHeight().pointerInput(Unit) {
                    detectTapGestures(onTap = { tapOffset ->
                        val maxAmount = maxOf((currentData.values.maxOrNull() ?: 0), (previousData.values.maxOrNull() ?: 0)).toFloat()
                        val xAxisPadding = 30.dp.toPx(); val chartHeight = size.height - xAxisPadding
                        val tappedMonthIndex = (tapOffset.x / monthWidth.toPx()).toInt()
                        if (tappedMonthIndex in 0..11) {
                            val tappedMonthName = Month.values()[tappedMonthIndex].name
                            val currentAmount = currentData.getOrDefault(tappedMonthName, 0)
                            val previousAmount = previousData.getOrDefault(tappedMonthName, 0)
                            if (currentAmount > 0 || previousAmount > 0) {
                                val currentHeight = if (maxAmount > 0) (currentAmount / maxAmount) * chartHeight else 0f
                                val prevHeight = if (maxAmount > 0) (previousAmount / maxAmount) * chartHeight else 0f
                                tooltipData = TooltipData(day = tappedMonthIndex + 1, currentAmount = currentAmount, previousAmount = previousAmount, tapPosition = tapOffset, barTopPosition = chartHeight - maxOf(currentHeight, prevHeight))
                            } else { tooltipData = null }
                        } else { tooltipData = null }
                    })
                }) {
                    val maxAmount = maxOf((currentData.values.maxOrNull() ?: 0), (previousData.values.maxOrNull() ?: 0)).toFloat()
                    val axisLabelPaint = Paint().asFrameworkPaint().apply { isAntiAlias = true; textSize = 12.sp.toPx(); color = android.graphics.Color.WHITE;}
                    val xAxisPadding = 30.dp.toPx(); val chartHeight = size.height - xAxisPadding
                    val nativeCanvas = drawContext.canvas.nativeCanvas
                    drawLine(color = Color.Gray, start = Offset(0f, chartHeight), end = Offset(size.width, chartHeight), strokeWidth = 2f)

                    val barWidth = monthWidth.toPx() * 0.4f

                    for (i in 0..11) {
                        val monthName = Month.values()[i].name
                        val xPosition = i * monthWidth.toPx() + (monthWidth.toPx() - barWidth * 2) / 2
                        val prevAmount = previousData.getOrDefault(monthName, 0).toFloat()
                        val currentAmount = currentData.getOrDefault(monthName, 0).toFloat()
                        val prevHeight = if (maxAmount > 0) (prevAmount / maxAmount) * chartHeight else 0f
                        val currentHeight = if (maxAmount > 0) (currentAmount / maxAmount) * chartHeight else 0f
                        drawRect(color = previousColor, topLeft = Offset(xPosition, chartHeight - prevHeight), size = Size(barWidth, prevHeight))
                        drawRect(color = currentColor, topLeft = Offset(xPosition + barWidth, chartHeight - currentHeight), size = Size(barWidth, currentHeight))
                        val labelX = xPosition + barWidth
                        drawLine(color = Color.Gray, start = Offset(labelX, chartHeight), end = Offset(labelX, chartHeight + 10f), strokeWidth = 2f)
                        nativeCanvas.drawText(monthNames[i], labelX, chartHeight + axisLabelPaint.textSize + 5.dp.toPx(), axisLabelPaint)
                    }

                    tooltipData?.let { data ->
                        val monthName = Month.of(data.day).name.lowercase().replaceFirstChar { it.titlecase() }
                        val fullDateText = "$monthName ${year.value}"
                        val text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)) { append(fullDateText) }
                            append("\n"); withStyle(style = SpanStyle(color = currentColor, fontSize = 12.sp)) { append("Current: ${formatCurrency(data.currentAmount)}") }
                            append("\n"); withStyle(style = SpanStyle(color = previousColor, fontSize = 12.sp)) { append("Previous: ${formatCurrency(data.previousAmount)}") }
                        }
                        val textLayoutResult = textMeasurer.measure(text); val tooltipWidth = textLayoutResult.size.width + 16.dp.toPx(); val tooltipHeight = textLayoutResult.size.height + 16.dp.toPx()
                        val tooltipX = (data.tapPosition.x - tooltipWidth / 2).coerceIn(0f, size.width - tooltipWidth); val tooltipY = (data.barTopPosition - tooltipHeight - 8.dp.toPx()).coerceAtLeast(0f)
                        drawRoundRect(color = Color.Black.copy(alpha = 0.8f), topLeft = Offset(tooltipX, tooltipY), size = Size(tooltipWidth, tooltipHeight), cornerRadius = CornerRadius(8.dp.toPx()))
                        drawText(textLayoutResult, topLeft = Offset(tooltipX + 8.dp.toPx(), tooltipY + 8.dp.toPx()))
                    }
                }
            }
        }
    }
}

// 🔹 DAILY BAR CHART (Updated)
// ===============================
@Composable
fun DailyBarChart(
    title: String,
    currentData: Map<Int, Int>,
    previousData: Map<Int, Int>,
    currentColor: Color,
    previousColor: Color,
    yearMonth: YearMonth,
    modifier: Modifier = Modifier
) {
    var tooltipData by remember { mutableStateOf<TooltipData?>(null) }
    val textMeasurer = rememberTextMeasurer()
    val scrollState = rememberScrollState()

    Column(modifier = modifier) {
        val yAxisTitlePaint = remember {
            Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                color = android.graphics.Color.WHITE
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textSize = 46f
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            // --- Y-Axis Title (Drawn outside the scrolling part) ---
            Canvas(modifier = Modifier.fillMaxHeight().width(40.dp)) {
                val nativeCanvas = drawContext.canvas.nativeCanvas
                nativeCanvas.withRotation(-90f) {
                    drawText(
                        title.uppercase(),
                        -(size.height / (1.45).toFloat()),
                        yAxisTitlePaint.textSize,
                        yAxisTitlePaint
                    )
                }
            }

            // --- Horizontally Scrollable Chart Area ---
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .horizontalScroll(scrollState)
            ) {
                val daysInMonth = yearMonth.lengthOfMonth()
                // Define a fixed width for each day's group of bars
                val dayWidth = 48.dp
                val chartCanvasWidth = dayWidth * daysInMonth

                Canvas(
                    modifier = Modifier
                        .width(chartCanvasWidth)
                        .fillMaxHeight()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { tapOffset ->
                                    val maxAmount = maxOf(
                                        (currentData.values.maxOrNull() ?: 0),
                                        (previousData.values.maxOrNull() ?: 0)
                                    ).toFloat()
                                    val xAxisPadding = 30.dp.toPx()
                                    val chartHeight = size.height - xAxisPadding

                                    val tappedDay = (tapOffset.x / dayWidth.toPx()).toInt() + 1

                                    if (tappedDay in 1..daysInMonth) {
                                        val currentAmount = currentData.getOrDefault(tappedDay, 0)
                                        val previousAmount = previousData.getOrDefault(tappedDay, 0)
                                        if (currentAmount > 0 || previousAmount > 0) {
                                            val currentHeight = if (maxAmount > 0) (currentAmount / maxAmount) * chartHeight else 0f
                                            val prevHeight = if (maxAmount > 0) (previousAmount / maxAmount) * chartHeight else 0f
                                            val barTop = chartHeight - maxOf(currentHeight, prevHeight)

                                            tooltipData = TooltipData(
                                                day = tappedDay,
                                                currentAmount = currentAmount,
                                                previousAmount = previousAmount,
                                                tapPosition = Offset(tapOffset.x, barTop),
                                                barTopPosition = barTop
                                            )
                                        } else {
                                            tooltipData = null
                                        }
                                    } else {
                                        tooltipData = null
                                    }
                                }
                            )
                        }
                ) {
                    val maxAmount = maxOf(
                        (currentData.values.maxOrNull() ?: 0),
                        (previousData.values.maxOrNull() ?: 0)
                    ).toFloat()
                    val axisLabelPaint = Paint().asFrameworkPaint().apply {
                        isAntiAlias = true; textSize = 12.sp.toPx(); color = android.graphics.Color.WHITE;
                    }
                    val xAxisPadding = 30.dp.toPx()
                    val chartHeight = size.height - xAxisPadding
                    val nativeCanvas = drawContext.canvas.nativeCanvas

                    drawLine(color = Color.Gray, start = Offset(0f, chartHeight), end = Offset(size.width, chartHeight), strokeWidth = 2f)

                    val barWidth = dayWidth.toPx() * 0.4f

                    for (day in 1..daysInMonth) {
                        val xPosition = (day - 1) * dayWidth.toPx() + (dayWidth.toPx() - barWidth * 2) / 2

                        val prevAmount = previousData.getOrDefault(day, 0).toFloat()
                        val currentAmount = currentData.getOrDefault(day, 0).toFloat()
                        val prevHeight = if (maxAmount > 0) (prevAmount / maxAmount) * chartHeight else 0f
                        val currentHeight = if (maxAmount > 0) (currentAmount / maxAmount) * chartHeight else 0f

                        drawRect(color = previousColor, topLeft = Offset(xPosition, chartHeight - prevHeight), size = Size(barWidth, prevHeight))
                        drawRect(color = currentColor, topLeft = Offset(xPosition + barWidth, chartHeight - currentHeight), size = Size(barWidth, currentHeight))

                        // Draw label for every day
                        val labelX = xPosition + barWidth
                        drawLine(color = Color.Gray, start = Offset(labelX, chartHeight), end = Offset(labelX, chartHeight + 10f), strokeWidth = 2f)
                        nativeCanvas.drawText("$day", labelX, chartHeight + axisLabelPaint.textSize + 5.dp.toPx(), axisLabelPaint)
                    }

                    // --- Draw Tooltip ---
                    tooltipData?.let { data ->
                        val date = yearMonth.atDay(data.day)
                        val fullDateText = date.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"))
                        val currentAmountText = "Current: ${formatCurrency(data.currentAmount)}"
                        val previousAmountText = "Previous: ${formatCurrency(data.previousAmount)}"
                        val text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)) { append(fullDateText) }
                            append("\n")
                            withStyle(style = SpanStyle(color = currentColor, fontSize = 12.sp)) { append(currentAmountText) }
                            append("\n")
                            withStyle(style = SpanStyle(color = previousColor, fontSize = 12.sp)) { append(previousAmountText) }
                        }
                        val textLayoutResult = textMeasurer.measure(text)
                        val tooltipWidth = textLayoutResult.size.width + 16.dp.toPx()
                        val tooltipHeight = textLayoutResult.size.height + 16.dp.toPx()
                        val tooltipX = (data.tapPosition.x - tooltipWidth / 2).coerceIn(0f, size.width - tooltipWidth)
                        val tooltipY = (data.barTopPosition - tooltipHeight - 8.dp.toPx()).coerceAtLeast(0f)
                        drawRoundRect(color = Color.Black.copy(alpha = 0.8f), topLeft = Offset(tooltipX, tooltipY), size = Size(tooltipWidth, tooltipHeight), cornerRadius = CornerRadius(8.dp.toPx()))
                        drawText(textLayoutResult, topLeft = Offset(tooltipX + 8.dp.toPx(), tooltipY + 8.dp.toPx()))
                    }
                }
            }
        }
    }
}
@Composable
fun BarChart(income: Int, prevIncome: Int, expense: Int, prevExpense: Int, modifier: Modifier = Modifier) {
    val allValues = listOf(income, prevIncome, expense, prevExpense).map { it.toFloat() }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Main canvas for drawing the bars
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            val barWidth = size.width / 4
            val maxAmount = allValues.maxOrNull() ?: 1f

            // Define colors
            val incomeColor = Color(0xFF00FF80)
            val prevIncomeColor = Color(0xFF006B36)
            val expenseColor = Color(0xFFFF6060)
            val prevExpenseColor = Color(0xFF8B2B2B)

            // Calculate bar heights
            val incomeHeight = (income.toFloat() / maxAmount) * size.height
            val prevIncomeHeight = (prevIncome.toFloat() / maxAmount) * size.height
            val expenseHeight = (expense.toFloat() / maxAmount) * size.height
            val prevExpenseHeight = (prevExpense.toFloat() / maxAmount) * size.height

            // --- Income Bars (Conditional Overlapping) ---
            if (income > prevIncome) {
                // Current is larger, draw it in the back
                drawRect(
                    color = incomeColor,
                    topLeft = Offset(x = size.width * 0.25f - barWidth / 2, y = size.height - incomeHeight),
                    size = Size(width = barWidth, height = incomeHeight)
                )
                // Previous is smaller, draw it in the front
                drawRect(
                    color = prevIncomeColor,
                    topLeft = Offset(x = size.width * 0.25f - barWidth / 2, y = size.height - prevIncomeHeight),
                    size = Size(width = barWidth, height = prevIncomeHeight)
                )
            } else {
                // Previous is larger (or equal), draw it in the back
                drawRect(
                    color = prevIncomeColor,
                    topLeft = Offset(x = size.width * 0.25f - barWidth / 2, y = size.height - prevIncomeHeight),
                    size = Size(width = barWidth, height = prevIncomeHeight)
                )
                // Current is smaller, draw it in the front
                drawRect(
                    color = incomeColor,
                    topLeft = Offset(x = size.width * 0.25f - barWidth / 2, y = size.height - incomeHeight),
                    size = Size(width = barWidth, height = incomeHeight)
                )
            }

            // --- Expense Bars (Conditional Overlapping) ---
            if (expense > prevExpense) {
                // Current is larger, draw it in the back
                drawRect(
                    color = expenseColor,
                    topLeft = Offset(x = size.width * 0.75f - barWidth / 2, y = size.height - expenseHeight),
                    size = Size(width = barWidth, height = expenseHeight)
                )
                // Previous is smaller, draw it in the front
                drawRect(
                    color = prevExpenseColor,
                    topLeft = Offset(x = size.width * 0.75f - barWidth / 2, y = size.height - prevExpenseHeight),
                    size = Size(width = barWidth, height = prevExpenseHeight)
                )
            } else {
                // Previous is larger (or equal), draw it in the back
                drawRect(
                    color = prevExpenseColor,
                    topLeft = Offset(x = size.width * 0.75f - barWidth / 2, y = size.height - prevExpenseHeight),
                    size = Size(width = barWidth, height = prevExpenseHeight)
                )
                // Current is smaller, draw it in the front
                drawRect(
                    color = expenseColor,
                    topLeft = Offset(x = size.width * 0.75f - barWidth / 2, y = size.height - expenseHeight),
                    size = Size(width = barWidth, height = expenseHeight)
                )
            }
        }

        // Row for labels below the chart (no changes needed)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Income", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = formatCurrency(income), color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Expense", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = formatCurrency(expense), color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
        }
    }
}


// ===============================
// 🔹 PREVIEW
// ===============================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AnalyzeAndAdviceScreenPreview() {
    // Add dummy data for the preview to work
    TransactionLists = mutableListOf(
        TransactionData("1", "expense", "Groceries", 1, "OCTOBER", 2025, R.drawable.cash_icon, 500000),
        TransactionData("2", "expense", "Electronics", 2, "OCTOBER", 2025, R.drawable.cash_icon, 1200000),
        TransactionData("3", "income", "Salary", 1, "OCTOBER", 2025, R.drawable.cash_icon, 5000000),
        TransactionData("4", "income", "Salary", 1, "SEPTEMBER", 2025, R.drawable.cash_icon, 4800000)
    )
    AnalyzeAndAdviceScreen(navController = rememberNavController())
}