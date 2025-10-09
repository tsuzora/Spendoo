//package com.example.spendoov2
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.example.spendoov2.ui.theme.*
//
//@Composable
//fun AddTransactionScreen(navController: NavController) {
//    var transactionType by remember { mutableStateOf<String?>("income") }
//
//    // State for the input fields
//    var amount by remember { mutableStateOf(TextFieldValue("")) }
//    var category by remember { mutableStateOf(TextFieldValue("")) }
//    var paymentMethod by remember { mutableStateOf(TextFieldValue("")) }
//
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        GreenDark,
//                        GreenMid,
//                        GreenLight
//                    )
//                )
//            )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            // Top Bar
//            AddTransactionTopBar(navController)
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Income / Expense Selector
//            IncomeExpenseAddTransactionNavBar(
//                activeTab = transactionType,
//                onTabSelected = { newTab -> transactionType = newTab }
//            )
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Date and Time Fields
//            DateTimeFields()
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Input Fields
//            TransactionInput(label = "Rp", placeholder = "Amount", state = amount, onValueChange = { amount = it })
//            Divider(color = Color.White.copy(alpha = 0.5f), thickness = 1.dp)
//            TransactionInput(placeholder = "Category", state = category, onValueChange = { category = it })
//            Divider(color = Color.White.copy(alpha = 0.5f), thickness = 1.dp)
//            TransactionInput(placeholder = "Payment Method", state = paymentMethod, onValueChange = { paymentMethod = it })
//            Divider(color = Color.White.copy(alpha = 0.5f), thickness = 1.dp)
//
//        }
//
//        // Save Button
//        FloatingActionButton(
//            onClick = { /* TODO: Handle save action */ },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(32.dp)
//                .size(64.dp),
//            containerColor = GreenLight,
//            shape = RoundedCornerShape(16.dp)
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_save),
//                contentDescription = "Save Transaction",
//                tint = Color.White,
//                modifier = Modifier.size(32.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun AddTransactionTopBar(navController: NavController) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        IconButton(onClick = { navController.popBackStack() }) {
//            Icon(
//                painter = painterResource(id = R.drawable.arrow_side_line_left),
//                contentDescription = "Back",
//                tint = Color.White
//            )
//        }
//        Text(
//            text = "Add Transaction",
//            style = poppinsTextStyle,
//            fontSize = 20.sp,
//            fontWeight = FontWeight.SemiBold,
//            color = Color.White
//        )
//        IconButton(onClick = { /* TODO: Handle delete action */ }) {
//            Icon(
//                painter = painterResource(id = R.drawable.plus_icon),
//                contentDescription = "Delete",
//                tint = Color.White
//            )
//        }
//    }
//}
//
//@Composable
//fun DateTimeFields() {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceAround,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_calendar),
//                contentDescription = "Date",
//                tint = Color.White
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = "August 16, 2025", color = Color.White, style = poppinsTextStyle)
//        }
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_time),
//                contentDescription = "Time",
//                tint = Color.White
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = "12.00 PM", color = Color.White, style = poppinsTextStyle)
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TransactionInput(
//    placeholder: String,
//    state: TextFieldValue,
//    onValueChange: (TextFieldValue) -> Unit,
//    label: String? = null
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//    ) {
//        if (label != null) {
//            Text(
//                text = label,
//                color = Color.White.copy(alpha = 0.7f),
//                style = poppinsTextStyle,
//                fontSize = 20.sp,
//                modifier = Modifier.padding(end = 8.dp)
//            )
//        }
//        TextField(
//            value = state,
//            onValueChange = onValueChange,
//            placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.7f)) },
//            textStyle = TextStyle(
//                color = Color.White,
//                fontSize = 20.sp,
//                fontFamily = poppinsFamily,
//                fontWeight = FontWeight.Medium
//            ),
//            colors = TextFieldDefaults.textFieldColors(
//                containerColor = Color.Transparent,
//                cursorColor = Color.White,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}
//
//
//@Composable
//fun IncomeExpenseAddTransactionNavBar(
//    activeTab: String?,
//    onTabSelected: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val incomeCorner = RoundedCornerShape(12.dp)
//    val expenseCorner = RoundedCornerShape(12.dp)
//
//    val incomeBrush = Brush.horizontalGradient(
//        colors = listOf(GreenDark, GreenLight)
//    )
//    val expenseBrush = Brush.horizontalGradient(
//        colors = listOf(RedDark, RedLight)
//    )
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        modifier = modifier
//            .fillMaxWidth()
//            .height(40.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxHeight()
//                .clip(incomeCorner)
//                .background(
//                    if (activeTab == "income") incomeBrush else GreyDark
//                )
//                .clickable { onTabSelected("income") },
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = "Income",
//                color = Color.White,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.SemiBold,
//                style = poppinsTextStyle
//            )
//        }
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxHeight()
//                .clip(expenseCorner)
//                .background(
//                    if (activeTab == "expense") expenseBrush else GreyDark
//                )
//                .clickable { onTabSelected("expense") },
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = "Expense",
//                color = Color.White,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.SemiBold,
//                style = poppinsTextStyle
//            )
//        }
//    }
//}
