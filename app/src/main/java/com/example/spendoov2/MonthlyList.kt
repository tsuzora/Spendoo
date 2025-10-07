package com.example.spendoov2

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendoov2.ui.theme.GreenMid
import com.example.spendoov2.ui.theme.poppinsTextStyle

@Composable
fun MonthlyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp, 0.dp)
    ) {
        val totalIncome = TotalIncome()
        val totalExpense = TotalExpense()
        CompositionLocalProvider(LocalTextStyle provides poppinsTextStyle) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(16.dp, 0.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Income",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Rp.$totalIncome",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Expense",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Rp.$totalExpense",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }
        }


        Spacer(modifier = modifier.padding(0.dp, 12.dp))
        LazyColumn(
            modifier = modifier.fillMaxWidth()
        ) {
           item {MonthlyContainer(15)}
           item {MonthlyContainer(14)}
           item {MonthlyContainer(13)}
        }

    }
}

@Composable
fun MonthlyContainer(
    date: Int,
    textStyle: TextStyle = poppinsTextStyle,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalTextStyle provides textStyle) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp, 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .height(24.dp)
            ) {
                Text(
                    text = "August $date",
                    fontSize = 18.sp,
                    modifier = modifier
                        .width(310.dp)
                )
                Image(
                    painter = painterResource(R.drawable.drop_arrow_green),
                    contentDescription = null,
                    modifier = modifier.size(18.dp)
                )
            }
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(GreenMid)
            )
        }
        MonthlyBanner(date)
    }
}


@Composable
fun MonthlyBanner(
    date: Int,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val filteredTransaction = TransactionLists.filter { it.date == date }
        val sortedTransactionsMonth = filteredTransaction.sortedByDescending { it.date }

       sortedTransactionsMonth.forEach {
           TransactionBanner(it) }
    }
}