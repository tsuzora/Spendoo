package com.example.spendoov2

import java.util.UUID
val type = listOf<String>("income", "expense")
val categoryIncome = mapOf<String, Int>(
    "Salary" to R.drawable.salary_income,
    "Investment" to R.drawable.investment_income,
    "Business" to R.drawable.business_income,
    "Royalty" to R.drawable.royalty_income,
    "Honorarium" to R.drawable.honorarium_income,
    "Bonus" to R.drawable.bonus_income,
    "Allowance" to R.drawable.allowance_income,
    "Fund" to R.drawable.fund_income,
    "Others" to R.drawable.others)
val categoryExpense = mapOf<String, Int>(
    "Electronics" to R.drawable.electronics_expense,
    "Furniture" to R.drawable.furniture_expense,
    "Groceries" to R.drawable.groceries_expense,
    "Education" to R.drawable.education_expense,
    "Clothes" to R.drawable.clothes_expense,
    "Energy" to R.drawable.energy_expense,
    "Internet" to R.drawable.internet_expense,
    "Transport" to R.drawable.transport_expense,
    "Others" to R.drawable.others)

fun generateTransactionData(num: Int) {
    if (TransactionLists.isNotEmpty()) return // Hanya generate data jika list kosong
    for (i in 1..num) {
        val transactionType = type.random()

        val catTransaction = if (transactionType == "income") {
            categoryIncome.entries.random()
        } else {
            categoryExpense.entries.random()
        }

        val amount = if (transactionType == "income") {
            (1..100).random()
        } else {
            (1..35).random()
        }

        val catName = catTransaction.key
        val icon = catTransaction.value
        val date = (1..28).random() // Menggunakan rentang tanggal yang lebih realistis
        val monthIndex = (0..11).random()
        val monthName = java.text.DateFormatSymbols().months[monthIndex]
        val year = 2025

        TransactionLists.add(
            TransactionData(
                id = UUID.randomUUID().toString(), // Menambahkan ID unik untuk setiap transaksi
                type = transactionType,
                category = catName,
                date = date,
                month = monthName,
                year = year,
                image = icon,
                amount = amount * 10000
            )
        )
    }
}


//fun TransactionData(num: Int) {
//    for (i in 1..num) {
//        val transactionType = type.random()
//
//         val catTransaction = if (transactionType == "income") {
//            categoryIncome.entries.random()
//        } else {
//            categoryExpense.entries.random()
//        }
//
//        val amount = if (transactionType == "income") {
//            (1..100).random()
//        } else {
//            (1..35).random()
//        }
//
//        val catName = catTransaction.key
//        val icon = catTransaction.value
//        val date = (10..15).random()
//        val month = Months.entries.random()
//
//        TransactionLists.add(
//            TransactionData(
//                transactionType,
//                catName,
//                date,
//                "September",
//                2025,
//                icon,
//                amount * 10000
//            )
//        )
//
//    }
//
//}
