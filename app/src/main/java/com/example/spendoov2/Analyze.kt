package com.example.spendoov2

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendoov2.ui.theme.MainBackgroundColor

// ===============================
// 🔹 DATA MODEL
// ===============================
data class AdviceCategory(
    val name: String,
    val percentage: Int,
    val color: Color,
    val advice: String
)

// ===============================
// 🔹 MAIN SCREEN
// ===============================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyzeAndAdviceScreen() {
    var selectedTab by remember { mutableStateOf("Advice") }
    var selectedPeriod by remember { mutableStateOf("Monthly") }
    var month by remember { mutableStateOf("AUGUST") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MainBackgroundColor),
        contentPadding = PaddingValues(
            start = 0.dp,
            end = 0.dp,
            top = 16.dp,
            bottom = 120.dp
        )
    ) {
        // 🔹 Header
        item {
            AnalyzeHeader(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                selectedPeriod = selectedPeriod,
                onPeriodSelected = { selectedPeriod = it },
                month = month
            )
        }

        // 🔹 Konten berdasarkan tab
        if (selectedTab == "Advice") {

            // Advice list & data dinamis
            val currentAdviceList = when (selectedPeriod) {
                "Yearly" -> listOf(
                    AdviceCategory("Salary", 20, Color(0xFFB0FFDD), "Your annual salary grew by 20%. Keep negotiating raises and improving your skillset."),
                    AdviceCategory("Investment", 12, Color(0xFF9CFFC4), "Your investments yielded solid results this year. Diversify to reduce risks next year."),
                    AdviceCategory("Business", 8, Color(0xFF7AB6FF), "Business profits increased by 8%. Consider reinvesting for higher scalability."),
                    AdviceCategory("Groceries", 15, Color(0xFFFFBEBE), "You spent 15% more on groceries this year. Plan better monthly budgets."),
                    AdviceCategory("Travel", 10, Color(0xFF7DFFFF), "Travel expenses rose by 10%. Try saving earlier to enjoy better deals next trips."),
                    AdviceCategory("Savings", 30, Color(0xFF54E871), "You saved 30% of your income. Excellent consistency! Keep this up.")
                )
                else -> listOf(
                    AdviceCategory("Salary", 12, Color(0xFF9CFFC4), "Your salary income dropped by 12%. Try reviewing your monthly cash flow and adjust your budget accordingly."),
                    AdviceCategory("Investment", 5, Color(0xFFBAFFC2), "Your investment returns fell by 5%. Recheck your portfolio allocation for better balance."),
                    AdviceCategory("Business", 2, Color(0xFF7AB6FF), "Business revenue decreased by 2%. Analyze your sales to find small improvement opportunities."),
                    AdviceCategory("Electronics", 11, Color(0xFFFFA8A8), "Spending on electronics increased by 11%. Try postponing non-essential gadget purchases."),
                    AdviceCategory("Allowance", 1, Color(0xFFFFFF70), "Allowance usage increased by 1%. Save a small portion instead of spending it all."),
                    AdviceCategory("Fund", 30, Color(0xFFFFA4A4), "Your fund balance dropped by 30%. Avoid unnecessary withdrawals and rebuild your savings.")
                )
            }

            // Income & Expense
            item {
                if (selectedPeriod == "Yearly") YearlyIncomeExpenseStats()
                else IncomeExpenseStats()
            }

            // Badge
            item {
                Spacer(Modifier.height(20.dp))
                if (selectedPeriod == "Yearly") YearlyBadge() else ExcellentBadge()
                Spacer(Modifier.height(20.dp))
                Text(
                    text = if (selectedPeriod == "Yearly") "Here are your yearly insights!" else "Here are some advice that you could use!",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Divider(
                    color = Color(0xFF1AFF9C),
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                        .fillMaxWidth()
                )
            }

            // Advice list
            items(currentAdviceList) { category ->
                AdviceCard(category)
            }

            // Footer
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedPeriod == "Yearly")
                            "🏅 You maintained great performance this year!"
                        else
                            "🏆 The Others Are Great! Consistency is The Key!",
                        color = Color(0xFFB0FFC1),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            item { StatisticsScreen(selectedPeriod) }
        }
    }
}

// ===============================
// 🔹 HEADER SECTION
// ===============================
@Composable
fun AnalyzeHeader(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit,
    month: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(25.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: Back to Home */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_side_line_left),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text("SPENDOO", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { /* TODO: Option menu */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.dot_option),
                    contentDescription = "Options",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(Modifier.height(15.dp))

        // Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Statistics", "Advice").forEach { tab ->
                val isSelected = selectedTab == tab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) }
                ) {
                    Text(
                        text = tab,
                        color = if (isSelected) Color.White else Color(0xFFB8FFCD),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    Box(
                        modifier = Modifier
                            .height(3.dp)
                            .fillMaxWidth()
                            .background(
                                brush = if (isSelected)
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF004D26), Color(0xFF00FF80), Color(0xFF004D26))
                                    )
                                else Brush.horizontalGradient(listOf(Color(0xFF003320), Color(0xFF003D25)))
                            )
                    )
                }
            }
        }

        Spacer(Modifier.height(25.dp))

        // Period Selector
        val periodOptions = if (selectedTab == "Statistics")
            listOf("Daily", "Monthly", "Yearly")
        else
            listOf("Monthly", "Yearly")

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            periodOptions.forEach { period ->
                val isSelected = selectedPeriod == period
                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .background(
                            brush = if (isSelected)
                                Brush.horizontalGradient(listOf(Color(0xFF54E871), Color(0xFF2F9944)))
                            else
                                Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent)),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onPeriodSelected(period) }
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Text(
                        period,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 15.sp
                    )
                }
            }
        }

        // Month navigation hanya untuk Advice
        if (selectedTab == "Advice") {
            Spacer(Modifier.height(25.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("<", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(end = 60.dp))
                Text(month, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(">", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 60.dp))
            }
        }
    }
}

// ===============================
// 🔹 YEARLY INCOME / EXPENSE STATS
// ===============================
// ===============================
// 🔹 YEARLY INCOME / EXPENSE STATS (DENGAN CHART)
// ===============================
@Composable
fun YearlyIncomeExpenseStats() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // ===============================
        // 🔹 INCOME SECTION (YEARLY)
        // ===============================
        Text(
            "Here's What You've Gained This Year",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Divider(
            color = Color(0xFF003320),
            thickness = 2.dp,
            modifier = Modifier
                .padding(top = 4.dp, bottom = 14.dp)
                .fillMaxWidth()
        )

        // 🔹 TOTAL + PERSENTASE
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                Text("TOTAL:", color = Color(0xFF2F9944), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(6.dp))
                Text("Rp68.450.000", color = Color(0xFFEDFFF5), fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("▲", color = Color(0xFF35FF4D), fontSize = 16.sp, modifier = Modifier.padding(end = 4.dp))
                Text(
                    "Your total income grew by +15% compared to last year.",
                    color = Color(0xFF35FF4D),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // 🔹 Donut Chart (Income)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleDonutChart(
                colors = listOf(
                    Color(0xFF9CFFC4), // Salary
                    Color(0xFF7DFFFF), // Business
                    Color(0xFF54E871), // Investment
                    Color(0xFFB8FFCD), // Bonus
                    Color(0xFF2F9944)  // Others
                )
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 30.dp)
            ) {
                LegendItem(Color(0xFF9CFFC4), "Salary")
                LegendItem(Color(0xFF7DFFFF), "Business")
                LegendItem(Color(0xFF54E871), "Investment")
                LegendItem(Color(0xFFB8FFCD), "Bonus")
                LegendItem(Color(0xFF2F9944), "Others")
            }
        }

        Spacer(Modifier.height(30.dp))

        // ===============================
        // 🔹 EXPENSE SECTION (YEARLY)
        // ===============================
        Text(
            "Here's What You've Spent This Year",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Divider(
            color = Color(0xFF3E0C0C),
            thickness = 2.dp,
            modifier = Modifier
                .padding(top = 4.dp, bottom = 14.dp)
                .fillMaxWidth()
        )

        // 🔹 TOTAL + PERSENTASE
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                Text("TOTAL:", color = Color(0xFFFF2626), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(6.dp))
                Text("Rp45.210.000", color = Color(0xFFEDFFF5), fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("▼", color = Color(0xFFDB2B2E), fontSize = 16.sp, modifier = Modifier.padding(end = 4.dp))
                Text(
                    "Your yearly expenses increased by +9% compared to last year.",
                    color = Color(0xFFFF5E5E),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // 🔹 Donut Chart (Expense)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleDonutChart(
                colors = listOf(
                    Color(0xFFFFA8A8), // Electronics
                    Color(0xFFFFC07F), // Groceries
                    Color(0xFFFF8A8A), // Utilities
                    Color(0xFFFF77FF), // Entertainment
                    Color(0xFFFCFF77)  // Travel
                )
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 30.dp)
            ) {
                LegendItem(Color(0xFFFFA8A8), "Electronics")
                LegendItem(Color(0xFFFFC07F), "Groceries")
                LegendItem(Color(0xFFFF8A8A), "Utilities")
                LegendItem(Color(0xFFFF77FF), "Entertainment")
                LegendItem(Color(0xFFFCFF77), "Travel")
            }
        }
    }
}


// ===============================
// 🔹 BADGES
// ===============================
@Composable
fun ExcellentBadge() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00FF80)),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🏆 EXCELLENT!", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Your statistics are EXCELLENT!", color = Color.Black, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun YearlyBadge() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC107)),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🏅 GREAT YEAR!", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("You’ve maintained excellent financial habits all year long.", color = Color.Black, textAlign = TextAlign.Center)
        }
    }
}

// ===============================
// 🔹 ADVICE CARD
// ===============================
@Composable
fun AdviceCard(category: AdviceCategory) {
    Card(
        colors = CardDefaults.cardColors(containerColor = category.color),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp, horizontal = 16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(category.name, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("▼ ${category.percentage}%", color = Color(0xFFD40000), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(6.dp))
            Text(category.advice, color = Color.Black.copy(alpha = 0.8f), fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}
// ===============================
// 🔹 MONTHLY INCOME / EXPENSE STATS
// ===============================
// ===============================
// 🔹 MONTHLY INCOME / EXPENSE STATS
// ===============================
@Composable
fun IncomeExpenseStats() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(25.dp))

        // ===============================
        // 🔹 INCOME SECTION
        // ===============================
        Text(
            "Here's What You've Gained in August",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Divider(
            color = Color(0xFF003320),
            thickness = 2.dp,
            modifier = Modifier
                .padding(top = 4.dp, bottom = 14.dp)
                .fillMaxWidth()
        )

        // 🔹 TOTAL di Tengah
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                Text("TOTAL:", color = Color(0xFF2F9944), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(6.dp))
                Text("Rp5.853.000", color = Color(0xFFEDFFF5), fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("▲", color = Color(0xFF359A41), fontSize = 16.sp, modifier = Modifier.padding(end = 4.dp))
                Text(
                    "Your total income increased by +12% compared to July.",
                    color = Color(0xFF35FF4D),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // 🔹 Donut Chart + Legend (Income)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            SimpleDonutChart(
                colors = listOf(
                    Color(0xFF9CFFC4), // Salary
                    Color(0xFFFFFF70), // Allowance
                    Color(0xFFBAFFC2), // Investment
                    Color(0xFFFF77FF), // Bonus
                    Color(0xFF7AB6FF), // Business
                    Color(0xFFFFA4A4), // Fund
                    Color(0xFF7DFFFF), // Honorarium
                    Color(0xFFBFBFBF)  // Royalty
                )
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 30.dp)
            ) {
                LegendItem(Color(0xFF9CFFC4), "Salary")
                LegendItem(Color(0xFFFFFF70), "Allowance")
                LegendItem(Color(0xFFBAFFC2), "Investment")
                LegendItem(Color(0xFFFF77FF), "Bonus")
                LegendItem(Color(0xFF7AB6FF), "Business")
                LegendItem(Color(0xFFFFA4A4), "Fund")
                LegendItem(Color(0xFF7DFFFF), "Honorarium")
                LegendItem(Color(0xFFBFBFBF), "Royalty")
            }
        }

        Spacer(Modifier.height(20.dp))

        // ===============================
        // 🔹 EXPENSE SECTION
        // ===============================
        Text(
            "Here's What You've Spent in August",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Divider(
            color = Color(0xFF003320),
            thickness = 2.dp,
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp)
                .fillMaxWidth()
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                Text("TOTAL:", color = Color(0xFFFF2626), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(6.dp))
                Text("Rp3.467.000", color = Color(0xFFEDFFF5), fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("▼", color = Color(0xFF861516), fontSize = 16.sp, modifier = Modifier.padding(end = 4.dp))
                Text(
                    "Your total expense increased by +33% compared to July.",
                    color = Color(0xFFDB2B2E),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // 🔹 Donut Chart + Legend (Expense)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleDonutChart(
                colors = listOf(
                    Color(0xFFFFA8A8), // Electronics
                    Color(0xFFFFBEBE), // Groceries
                    Color(0xFFFFC107), // Utilities
                    Color(0xFFFF77FF), // Entertainment
                    Color(0xFF9CFFC4), // Transportation
                    Color(0xFF7DFFFF)  // Dining
                )
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 30.dp)
            ) {
                LegendItem(Color(0xFFFFA8A8), "Electronics")
                LegendItem(Color(0xFFFFBEBE), "Groceries")
                LegendItem(Color(0xFFFFC107), "Utilities")
                LegendItem(Color(0xFFFF77FF), "Entertainment")
                LegendItem(Color(0xFF9CFFC4), "Transportation")
                LegendItem(Color(0xFF7DFFFF), "Dining")
            }
        }
    }
}

// 🔹 Legend Item Component
@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = Color(0xFF359A41), fontSize = 13.sp)
    }
}

// 🔹 Donut Chart
@Composable
fun SimpleDonutChart(colors: List<Color>) {
    Box(Modifier.size(150.dp).padding(8.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val sweep = 360f / colors.size
            colors.forEachIndexed { index, color ->
                drawArc(
                    color = color,
                    startAngle = index * sweep,
                    sweepAngle = sweep,
                    useCenter = true,
                    size = Size(size.width, size.height)
                )
            }
        }
        Box(Modifier.size(50.dp).background(Color(0xFF081C30), shape = CircleShape))
    }
}


// ===============================
// 🔹 STATISTICS SCREEN
// ===============================
@Composable
fun StatisticsScreen(selectedPeriod: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        Text("Statistics Overview ($selectedPeriod)", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Divider(color = Color(0xFF2F9944), thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp).padding(8.dp)) {
            val barColors = listOf(Color(0xFF00FF80), Color(0xFFFF6060))
            val barWidth = size.width / 6
            val maxHeight = size.height * 0.8f
            drawRect(barColors[0], topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.3f, size.height - maxHeight), size = Size(barWidth, maxHeight))
            drawRect(barColors[1], topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.6f, size.height - (maxHeight * 0.6f)), size = Size(barWidth, maxHeight * 0.6f))
        }
        Text("Your income increased by 12%, while your expenses grew by 33%.", color = Color(0xFFB8FFCD), fontSize = 14.sp, textAlign = TextAlign.Center)
    }
}

// ===============================
// 🔹 PREVIEW
// ===============================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AnalyzeAndAdviceScreenPreview() {
    AnalyzeAndAdviceScreen()
}
