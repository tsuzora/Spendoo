package com.example.spendoov2

import java.text.NumberFormat

fun AvailableBalance(): String? {
    val availableAmount = NumberFormat.getNumberInstance().format(
        TransactionLists.filter{ it.type == "income" }.sumOf { it.amount } -
                TransactionLists.filter { it.type == "expense" }.sumOf { it.amount })

    return availableAmount
}

fun TotalExpense(): String? {
    val totalExpense = NumberFormat.getNumberInstance().format(
        TransactionLists.filter{ it.type == "expense" }.sumOf { it.amount })

    return totalExpense
}
fun TotalIncome(): String? {
    val totalIncome = NumberFormat.getNumberInstance().format(
        TransactionLists.filter{ it.type == "income" }.sumOf { it.amount })

    return totalIncome
}