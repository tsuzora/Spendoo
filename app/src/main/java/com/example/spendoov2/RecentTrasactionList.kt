package com.example.spendoov2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendoov2.ui.theme.ExpenseBackgroundColor
import com.example.spendoov2.ui.theme.IncomeBackgroundColor
import com.example.spendoov2.ui.theme.poppinsTextStyle
import java.time.LocalDate
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale

data class TransactionData(
    val id: String,
    val type: String,
    val category: String,
    val date: Int,
    val month: String,
    val year: Int,
    val image: Int,
    val amount: Int
)

var TransactionLists = mutableListOf<TransactionData>()

@Composable
fun RecentTransactionList(
    filterType: String?,
    selectedDate: LocalDate?,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier)
{
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val typedTransactions = if (filterType == null) {
            TransactionLists
        } else {
            TransactionLists.filter { it.type == filterType }
        }

        //Filter berdasarkan tanggal yang dipilih
        val dateFilteredTransactions = if (selectedDate != null) {
            typedTransactions.filter {
                it.date == selectedDate.dayOfMonth &&
                        it.month.equals(selectedDate.month.name, ignoreCase = true) &&
                        it.year == selectedDate.year
            }
        } else {
            // Jika tidak ada tanggal dipilih (di tab "all"), tampilkan 7 hari terakhir
            val oneWeekAgo = LocalDate.now().minusDays(7)
            typedTransactions.filter {
                // Konversi data transaksi ke LocalDate untuk perbandingan
                val transactionDate = LocalDate.of(it.year,
                    Month.valueOf(it.month.uppercase(Locale.ROOT)).ordinal + 1,
                    it.date)
                !transactionDate.isBefore(oneWeekAgo)
            }
        }

        val sortedTransactions = dateFilteredTransactions.sortedByDescending { it.date }

        if (sortedTransactions.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No transaction",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(32.dp, 4.dp)
            ) {
                items(sortedTransactions) { transaction ->
                    TransactionBanner(
                        transaction = transaction,
                        onTransactionClick = { onTransactionClick(transaction.id) }
                    )
                }
            }
        }
    }
}

//        val sortedTransactionsMonth = transactionShown.sortedByDescending { it.date }
//
//        LazyColumn(
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(32.dp, 4.dp)
//        ) {
//            items(sortedTransactionsMonth) { transactions ->
//
//                TransactionBanner(transaction = transactions)
//            }
//        }
//    }
//}

@Composable
fun TransactionBanner(
    transaction: TransactionData,
    onTransactionClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var bgColor: Brush = ExpenseBackgroundColor

    bgColor = if (transaction.type == "expense") {
        ExpenseBackgroundColor
    } else {
        IncomeBackgroundColor
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 6.dp)
            .clip(RoundedCornerShape(6.dp))
            .height(64.dp)
            .background(bgColor)
            .clickable{onTransactionClick()}
    ) {
        Image(
            painter = painterResource(transaction.image),
            contentDescription = null,
            modifier = modifier
                .padding(8.dp)
                .size(42.dp)
        )

        CompositionLocalProvider(LocalTextStyle provides poppinsTextStyle) {
            Column {
                Text(
                    text = transaction.category,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = "${transaction.date} ${transaction.month} ${transaction.year}",
                    color = Color.Black
                )
            }
            Spacer(modifier = modifier.weight(1f))

            Text(
                text = "Rp${transaction.amount}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = modifier
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun TransactionBannerPreview() {
    RecentTransactionList(filterType = null, selectedDate = null, onTransactionClick = {})
}
