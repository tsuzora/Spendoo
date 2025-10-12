package com.example.spendoov2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendoov2.ui.theme.GreenDark
import com.example.spendoov2.ui.theme.GreyDark
import com.example.spendoov2.ui.theme.poppinsTextStyle


@Composable
fun IncomeExpenseNavBar(
    activeTab: String?,
    onTabSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {

    val incomeCorner = RoundedCornerShape(24.dp, 0.dp, 0.dp, 24.dp)
    val expenseCorner = RoundedCornerShape(0.dp, 24.dp, 24.dp, 0.dp)
    CompositionLocalProvider(LocalTextStyle provides poppinsTextStyle) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(28.dp, 0.dp)
                .height(32.dp)
        ) {
            IncomeExpenseTab(
                text = "Income",
                shape = incomeCorner,
                isActive = activeTab == "income",
                onClick = {
                    val newTab = if (activeTab == "income") null else "income"
                    onTabSelected(newTab)
                }
            )
            IncomeExpenseTab(
                text = "Expense",
                shape = expenseCorner,
                isActive = activeTab == "expense",
                onClick = {
                    val newTab = if (activeTab == "expense") null else "expense"
                    onTabSelected(newTab)
                }
            )
        }
    }
}

@Composable
fun IncomeExpenseTab(
    text: String,
    shape: Shape,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(165.dp)
            .height(28.dp)
            .clip(shape)
            .background(
                if (isActive) GreenDark else GreyDark
            )
            .clickable(onClick = onClick)

    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier
                .align(Alignment.Center)
        )
    }
}