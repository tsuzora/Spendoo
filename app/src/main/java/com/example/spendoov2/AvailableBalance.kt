package com.example.spendoov2

import java.text.NumberFormat

// --- UBAH FUNGSI INI ---
fun AvailableBalance(transactions: List<TransactionData>): String? {
    val availableAmount = NumberFormat.getNumberInstance().format(
        transactions.filter{ it.type == "income" }.sumOf { it.amount } - // <-- GANTI 'TransactionLists'
                transactions.filter { it.type == "expense" }.sumOf { it.amount }) // <-- GANTI 'TransactionLists'

    return availableAmount
}

// --- UBAH FUNGSI INI ---
fun TotalExpense(transactions: List<TransactionData>): String? {
    val totalExpense = NumberFormat.getNumberInstance().format(
        transactions.filter{ it.type == "expense" }.sumOf { it.amount }) // <-- GANTI 'TransactionLists'

    return totalExpense
}

// --- UBAH FUNGSI INI ---
fun TotalIncome(transactions: List<TransactionData>): String? {
    val totalIncome = NumberFormat.getNumberInstance().format(
        transactions.filter{ it.type == "income" }.sumOf { it.amount }) // <-- GANTI 'TransactionLists'

    return totalIncome
}


/*
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
}*/
