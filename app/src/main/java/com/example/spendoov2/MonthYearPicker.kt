package com.example.spendoov2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.Month
import java.util.Locale

@Composable
fun MonthYearPickerOverlay(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    var displayedYear by remember { mutableStateOf(initialDate.year) }
    val currentMonth = initialDate.month

    val months = Month.values()

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Header: Navigasi Tahun
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_side_line_left),
                    contentDescription = "Previous Year",
                    modifier = Modifier.size(24.dp).clickable { displayedYear-- }
                )
                Text(
                    text = displayedYear.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Icon(
                    painter = painterResource(id = R.drawable.arrow_side_line_right),
                    contentDescription = "Next Year",
                    modifier = Modifier.size(24.dp).clickable { displayedYear++ }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            // Grid Bulan
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(months) { month ->
                    val isSelected = month == currentMonth && displayedYear == initialDate.year
                    val monthName = month.name.lowercase().replaceFirstChar { it.titlecase() }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) AddIncomeBGColor else Color.Transparent)
                            .clickable {
                                val newDate = LocalDate.of(displayedYear, month, 1)
                                onDateSelected(newDate)
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = monthName.take(3),
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
