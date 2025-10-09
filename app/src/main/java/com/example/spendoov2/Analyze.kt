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
        Text("SPENDOO", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("You Earn, We Learn", color = Color(0xFFB8FFCD), fontSize = 13.sp)
        Spacer(Modifier.height(16.dp))

        // Tabs
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("Statistics", "Advice").forEach { tab ->
                Text(
                    text = tab,
                    color = if (selectedTab == tab) Color(0xFF54FFB0) else Color.White,
                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.clickable { onTabSelected(tab) }.padding(6.dp)
                )
            }
        }
        Divider(color = Color(0xFF54FFB0), thickness = 2.dp)
        Spacer(Modifier.height(12.dp))

        // Period Selector
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            listOf("Daily", "Monthly", "Yearly").forEach { period ->
                val isSelected = selectedPeriod == period
                Box(
                    Modifier
                        .padding(horizontal = 6.dp)
                        .background(
                            color = if (isSelected) Color(0xFF00FF80) else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onPeriodSelected(period) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        period,
                        color = if (isSelected) Color.Black else Color.White,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Month Navigation
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("<", color = Color.White, fontSize = 20.sp, modifier = Modifier.clickable { })
            Text(month, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(">", color = Color.White, fontSize = 20.sp, modifier = Modifier.clickable { })
        }
    }
}

// ===============================
// 🔹 INCOME / EXPENSE STATS
// ===============================
@Composable
fun IncomeExpenseStats() {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(16.dp))

        // INCOME
        Text("Here's What You've Gained in August", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(4.dp))
        Text("TOTAL: Rp5.853.000", color = Color(0xFF00FF80), fontWeight = FontWeight.Bold)
        Text("▲ Your total income increased by +12% compared to July.", color = Color(0xFF00FF80), fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))
        SimpleDonutChart(colors = listOf(Color(0xFF00FF80), Color.Yellow, Color.Cyan))

        Spacer(Modifier.height(20.dp))

        // EXPENSE
        Text("Here's What You've Spent in August", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(4.dp))
        Text("TOTAL: Rp3.467.000", color = Color(0xFFFF6060), fontWeight = FontWeight.Bold)
        Text("▼ Your total expense increased by +33% compared to July.", color = Color(0xFFFF6060), fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))
        SimpleDonutChart(colors = listOf(Color(0xFFFF6060), Color.Magenta, Color(0xFFFFC107)))
    }
}

// ===============================
// 🔹 DONUT CHART
// ===============================
@Composable
fun SimpleDonutChart(colors: List<Color>) {
    Box(Modifier.size(120.dp).padding(8.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(100.dp)) {
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