package com.example.spendoov2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendoov2.ui.theme.GreenMid
import com.example.spendoov2.ui.theme.poppinsTextStyle
import java.time.LocalDate

@Composable
fun MonthlyList(
    selectedDate: LocalDate,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val monthName = selectedDate.month.name.lowercase().replaceFirstChar { it.titlecase() }
    val year = selectedDate.year

    // Filter transaksi berdasarkan bulan dan tahun yang dipilih
    val monthlyTransactions = TransactionLists.filter {
        it.month.equals(monthName, ignoreCase = true) && it.year == year
    }

    // Hitung total income dan expense untuk bulan yang dipilih
    val totalIncome = monthlyTransactions.filter { it.type == "income" }.sumOf { it.amount }
    val totalExpense = monthlyTransactions.filter { it.type == "expense" }.sumOf { it.amount }

    // Kelompokkan transaksi berdasarkan tanggal
    val groupedByDate = monthlyTransactions.groupBy { it.date }.toSortedMap(compareByDescending { it })

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp, 0.dp)
    ) {
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
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(text = "Income", fontSize = 16.sp)
                    Text(text = "Rp$totalIncome", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(text = "Expense", fontSize = 16.sp)
                    Text(text = "Rp$totalExpense", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = modifier.padding(0.dp, 12.dp))

        if (monthlyTransactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No transaction this month",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(modifier = modifier.fillMaxWidth()) {
                items(groupedByDate.entries.toList()) { (date, transactions) ->
                    MonthlyContainer(
                        date = date,
                        monthName = monthName,
                        transactions = transactions,
                        onTransactionClick = onTransactionClick
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyContainer(
    date: Int,
    monthName: String,
    transactions: List<TransactionData>,
    onTransactionClick: (String) -> Unit,
    textStyle: TextStyle = poppinsTextStyle,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

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
                    .clickable { isExpanded = !isExpanded } // Toggle expand/collapse
            ) {
                Text(
                    text = "$monthName $date",
                    fontSize = 18.sp,
                    modifier = modifier.weight(1f)
                )
                Image(
                    painter = painterResource(
                        if (isExpanded) R.drawable.drop_arrow_green else R.drawable.arrow_side_line_right
                    ),
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
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
        if (isExpanded) {
            Column(modifier = modifier.fillMaxWidth()) {
                transactions.forEach { transaction ->
                    TransactionBanner(
                        transaction = transaction,
                        onTransactionClick = { onTransactionClick(transaction.id) }
                    )
                }
            }
        }
    }
}


//@Composable
//fun MonthlyList(modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(32.dp, 0.dp)
//    ) {
//        val totalIncome = TotalIncome()
//        val totalExpense = TotalExpense()
//        CompositionLocalProvider(LocalTextStyle provides poppinsTextStyle) {
//            Column(
//                verticalArrangement = Arrangement.Center,
//                modifier = modifier
//                    .fillMaxWidth()
//                    .height(48.dp)
//                    .padding(16.dp, 0.dp)
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    modifier = modifier
//                        .fillMaxWidth()
//                ) {
//                    Text(
//                        text = "Income",
//                        fontSize = 16.sp
//                    )
//                    Text(
//                        text = "Rp$totalIncome",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//                Row(
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    modifier = modifier
//                        .fillMaxWidth()
//                ) {
//                    Text(
//                        text = "Expense",
//                        fontSize = 16.sp
//                    )
//                    Text(
//                        text = "Rp$totalExpense",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//
//            }
//        }
//
//
//        Spacer(modifier = modifier.padding(0.dp, 12.dp))
//        LazyColumn(
//            modifier = modifier.fillMaxWidth()
//        ) {
//           item {MonthlyContainer(15)}
//           item {MonthlyContainer(14)}
//           item {MonthlyContainer(13)}
//        }
//
//    }
//}

//@Composable
//fun MonthlyContainer(
//    date: Int,
//    textStyle: TextStyle = poppinsTextStyle,
//    modifier: Modifier = Modifier
//) {
//    CompositionLocalProvider(LocalTextStyle provides textStyle) {
//        Column(
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(0.dp, 12.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = modifier
//                    .fillMaxWidth()
//                    .height(24.dp)
//            ) {
//                Text(
//                    text = "August $date",
//                    fontSize = 18.sp,
//                    modifier = modifier
//                        .width(310.dp)
//                )
//                Image(
//                    painter = painterResource(R.drawable.drop_arrow_green),
//                    contentDescription = null,
//                    modifier = modifier.size(18.dp)
//                )
//            }
//            Box(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .height(6.dp)
//                    .clip(RoundedCornerShape(8.dp))
//                    .background(GreenMid)
//            )
//        }
//        MonthlyBanner(date)
//    }
//}
//
//
//@Composable
//fun MonthlyBanner(
//    date: Int,
//    modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//    ) {
//        val filteredTransaction = TransactionLists.filter { it.date == date }
//        val sortedTransactionsMonth = filteredTransaction.sortedByDescending { it.date }
//
//       sortedTransactionsMonth.forEach {
//           TransactionBanner(it) }
//    }
//}