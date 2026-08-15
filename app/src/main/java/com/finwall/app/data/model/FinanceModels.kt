package com.finwall.app.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsSubway
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class TransactionType {
    EXPENSE,
    INCOME,
    LENT,
    DEBT
}

data class CategoryOption(
    val name: String,
    val icon: ImageVector,
    val type: TransactionType = TransactionType.EXPENSE
)

data class TransactionItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String,
    val paymentMethod: String,
    val amount: Double,
    val type: TransactionType,
    val icon: ImageVector,
    val timestamp: Long = System.currentTimeMillis(),
    val formattedDate: String = formatTimestampToDate(timestamp),
    val formattedTime: String = formatTimestampToTime(timestamp)
) {
    val displayAmount: String
        get() = when (type) {
            TransactionType.EXPENSE -> String.format(Locale.US, "- AED %.2f", amount)
            TransactionType.INCOME -> String.format(Locale.US, "+ AED %.2f", amount)
            TransactionType.LENT -> String.format(Locale.US, "AED %.2f", amount)
            TransactionType.DEBT -> String.format(Locale.US, "AED %.2f", amount)
        }
}

data class TransactionDateGroup(
    val dateLabel: String,
    val items: List<TransactionItem>
)

data class FinanceSummary(
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val totalLent: Double = 0.0,
    val totalDebt: Double = 0.0,
    val monthlyBudgetLimit: Double = 3000.0,
    val budgetUsagePercentage: Float = 0f,
    val recentTransactions: List<TransactionItem> = emptyList(),
    val categorySpendProportions: List<Float> = emptyList()
)

data class FinancialActivityLog(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val isHighlighted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

// Helper formatting functions
fun formatTimestampToDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val sdfDay = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val todayStr = sdfDay.format(Date(now))
    val txStr = sdfDay.format(Date(timestamp))

    return when {
        todayStr == txStr -> "Today"
        (todayStr.toLongOrNull() ?: 0) - (txStr.toLongOrNull() ?: 0) == 1L -> "Yesterday"
        else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(timestamp))
    }
}

fun formatTimestampToTime(timestamp: Long): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))
}

fun formatCurrency(amount: Double, type: TransactionType? = null): String {
    return when (type) {
        TransactionType.EXPENSE -> String.format(Locale.US, "- AED %,.2f", amount)
        TransactionType.INCOME -> String.format(Locale.US, "+ AED %,.2f", amount)
        TransactionType.LENT, TransactionType.DEBT -> String.format(Locale.US, "AED %,.2f", amount)
        null -> String.format(Locale.US, "AED %,.2f", amount)
    }
}

// Default curated category sets
val DefaultExpenseCategories = listOf(
    CategoryOption("Food & Dining", Icons.Default.Restaurant, TransactionType.EXPENSE),
    CategoryOption("Fast Food", Icons.Default.Fastfood, TransactionType.EXPENSE),
    CategoryOption("Groceries", Icons.Default.ShoppingCart, TransactionType.EXPENSE),
    CategoryOption("Travel & Transport", Icons.Default.DirectionsCar, TransactionType.EXPENSE),
    CategoryOption("Home & Rent", Icons.Default.Home, TransactionType.EXPENSE),
    CategoryOption("Bills & Utilities", Icons.AutoMirrored.Filled.ReceiptLong, TransactionType.EXPENSE),
    CategoryOption("Entertainment", Icons.Default.Movie, TransactionType.EXPENSE),
    CategoryOption("Education", Icons.Default.School, TransactionType.EXPENSE)
)

val DefaultIncomeCategories = listOf(
    CategoryOption("Salary", Icons.Default.Work, TransactionType.INCOME),
    CategoryOption("Investments", Icons.AutoMirrored.Filled.TrendingUp, TransactionType.INCOME),
    CategoryOption("Freelance", Icons.Default.Payment, TransactionType.INCOME),
    CategoryOption("Bonus & Gifts", Icons.Default.AttachMoney, TransactionType.INCOME)
)

val DefaultLentCategories = listOf(
    CategoryOption("Friend Loan", Icons.Default.Person, TransactionType.LENT),
    CategoryOption("Family Support", Icons.Default.Home, TransactionType.LENT),
    CategoryOption("Colleague", Icons.Default.Work, TransactionType.LENT),
    CategoryOption("Other", Icons.Default.Payment, TransactionType.LENT)
)

val DefaultDebtCategories = listOf(
    CategoryOption("Borrowed from Friend", Icons.Default.Person, TransactionType.DEBT),
    CategoryOption("Family Loan", Icons.Default.Home, TransactionType.DEBT),
    CategoryOption("Bank Loan", Icons.Default.Payment, TransactionType.DEBT),
    CategoryOption("Credit Advance", Icons.Default.CreditCard, TransactionType.DEBT)
)
