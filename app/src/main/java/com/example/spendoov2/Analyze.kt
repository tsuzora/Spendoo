package com.example.spendoov2

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.*
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
    var selectedTab by remember { mutableStateOf("Statistics") }
    var selectedPeriod by remember { mutableStateOf("Monthly") }
    var month by remember { mutableStateOf("AUGUST") }

    val adviceList = remember {
        listOf(
            AdviceCategory("Salary", 12, Color(0xFF9CFFC4), "Your salary income dropped by 12%. Try reviewing your monthly cash flow and adjust your budget accordingly."),
            AdviceCategory("Investment", 5, Color(0xFFBAFFC2), "Your investment returns fell by 5%. Recheck your portfolio allocation for better balance."),
            AdviceCategory("Business", 2, Color(0xFF7AB6FF), "Business revenue decreased by 2%. Analyze your sales to find small improvement opportunities."),
            AdviceCategory("Electronics", 11, Color(0xFFFFA8A8), "Spending on electronics increased by 11%. Try postponing non-essential gadget purchases."),
            AdviceCategory("Allowance", 1, Color(0xFFFFFF70), "Allowance usage increased by 1%. Save a small portion instead of spending it all."),
            AdviceCategory("Bonus", 2, Color(0xFFFF77FF), "Your bonus decreased by 2%. Consider saving or investing bonuses instead of spending them quickly."),
            AdviceCategory("Fund", 30, Color(0xFFFFA4A4), "Your fund balance dropped by 30%. Avoid unnecessary withdrawals and rebuild your savings."),
            AdviceCategory("Royalty", 10, Color(0xFFBFBFBF), "Royalty income fell by 10%. Review your licensing or content strategy for consistent returns."),
            AdviceCategory("Honorarium", 92, Color(0xFF7DFFFF), "Honorarium income dropped by 92%. Try finding consistent freelance or side projects to recover."),
            AdviceCategory("Groceries", 34, Color(0xFFFFBEBE), "Your grocery expenses rose by 34%. Plan meals and avoid impulse buying to manage better.")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MainBackgroundColor),
        contentPadding = PaddingValues(
            start = 0.dp,
            end = 0.dp,
            top = 16.dp,
            bottom = 120.dp // padding bawah yang lebih besar
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

        // 🔹 Statistik
        item {
            IncomeExpenseStats()
        }

        // 🔹 Badge + Intro Text
        item {
            Spacer(Modifier.height(20.dp))
            ExcellentBadge()
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Here are some advice that you could use!",
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

        // 🔹 Daftar Advice
        items(adviceList) { category ->
            AdviceCard(category)
        }

        // 🔹 Footer
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🏆 The Others Are Great! Consistency is The Key!",
                    color = Color(0xFFB0FFC1),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(30.dp))
        Text("SPENDOO", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("You Earn, We Learn", color = Color(0xFFB8FFCD), fontSize = 13.sp)
        Spacer(Modifier.height(25.dp))

        // Tabs
        // ===============================
// 🔹 CUSTOM TAB BAR
// ===============================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
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
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // 🔹 Animated gradient underline
                    Box(
                        modifier = Modifier
                            .height(3.dp)
                            .fillMaxWidth()
                            .background(
                                brush = if (isSelected)
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF004D26),  // hijau gelap ke kiri
                                            Color(0xFF00FF80),  // hijau terang di tengah
                                            Color(0xFF004D26)   // hijau gelap ke kanan
                                        )
                                    )
                                else
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF003320),
                                            Color(0xFF003D25)
                                        )
                                    )
                            )
                    )
                }
            }
        }


        Spacer(Modifier.height(10.dp))

        // Period Selector
        // ===============================
// 🔹 PERIOD SELECTOR (Monthly / Yearly)
// ===============================
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            listOf("Monthly", "Yearly").forEach { period ->
                val isSelected = selectedPeriod == period

                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .background(
                            brush = if (isSelected)
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF54E871), // kiri
                                        Color(0xFF2F9944)  // kanan
                                    ),
                                    startX = 0f,
                                    endX = Float.POSITIVE_INFINITY
                                )
                            else
                                Brush.horizontalGradient(
                                    colors = listOf(Color.Transparent, Color.Transparent)
                                ),
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


        Spacer(Modifier.height(10.dp))

        // Month Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "<",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { /* aksi ke bulan sebelumnya */ }
                    .padding(end = 60.dp) // jarak ke kanan
            )

            Text(
                month,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                ">",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { /* aksi ke bulan berikutnya */ }
                    .padding(start = 60.dp) // jarak ke kiri
            )
        }

    }
}

// ===============================
// 🔹 INCOME / EXPENSE STATS
// ===============================
@Composable
fun IncomeExpenseStats() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

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
                .padding(start = 0.dp, end = 2.dp, top = 4.dp, bottom = 14.dp)
                .fillMaxWidth()
        )
        Spacer(Modifier.height(0.dp))
        // INCOME

// 🔹 Bagian TOTAL + Nilai di Tengah
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "TOTAL:",
                    color = Color(0xFF2F9944),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Text(
                    text = "Rp5.853.000",
                    color = Color(0xFFEDFFF5),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "▲",
                    color = Color(0xFF359A41),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "Your total income increased by +12% compared to July.",
                    color = Color(0xFF35FF4D),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            // Donut Chart (Income)
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

            // Income Legend
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
                .padding(start = 0.dp, end = 2.dp, top = 4.dp, bottom = 16.dp)
                .fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "TOTAL:",
                    color = Color(0xFFFF2626),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Text(
                    text = "Rp3.467.000",
                    color = Color(0xFFEDFFF5),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "▼",
                    color = Color(0xFF861516),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "Your total income increased by +12% compared to July.",
                    color = Color(0xFFDB2B2E),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Donut Chart (Expense)
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

            // Expense Legend
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

// 🔹 LEGEND ITEM COMPONENT
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

// ===============================
// 🔹 DONUT CHART
// ===============================
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
// 🔹 EXCELLENT BADGE
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
            Spacer(Modifier.height(4.dp))
            Text("From the statistics, we've analyzed and graded your cash flow!", color = Color.Black, textAlign = TextAlign.Center)
            Text("Your statistics are EXCELLENT!", color = Color.Black, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)
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
// 🔹 PREVIEW
// ===============================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AnalyzeAndAdviceScreenPreview() {
    AnalyzeAndAdviceScreen()
}