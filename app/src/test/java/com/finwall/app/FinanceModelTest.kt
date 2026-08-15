package com.finwall.app
 
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Work
import com.finwall.app.data.model.TransactionItem
import com.finwall.app.data.model.TransactionType
import com.finwall.app.data.model.formatCurrency
import com.finwall.app.ui.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
 
class FinanceModelTest {
 
     @Test
     fun `formatCurrency formats amounts correctly`() {
         val expenseFormatted = formatCurrency(25.0, TransactionType.EXPENSE)
         val incomeFormatted = formatCurrency(3200.0, TransactionType.INCOME)
         val defaultFormatted = formatCurrency(1500.5)
 
         assertEquals("- AED 25.00", expenseFormatted)
         assertEquals("+ AED 3,200.00", incomeFormatted)
         assertEquals("AED 1,500.50", defaultFormatted)
     }
 
     @Test
    fun `MainViewModel correctly calculates total balance and updates on new transaction`() {
        val viewModel = MainViewModel()
        val initialSummary = viewModel.uiState.value.financeSummary

        // Initial state is a clean slate: 0 income, 0 expense, 0 balance
        assertEquals(0.0, initialSummary.totalIncome, 0.01)
        assertEquals(0.0, initialSummary.totalExpense, 0.01)
        assertEquals(0.0, initialSummary.totalLent, 0.01)
        assertEquals(0.0, initialSummary.totalDebt, 0.01)
        assertEquals(0.0, initialSummary.totalBalance, 0.01)

        // Add a new income of 1500.0
        viewModel.addTransaction(
            TransactionItem(
                title = "Salary",
                category = "Salary",
                paymentMethod = "Bank",
                amount = 1500.0,
                type = TransactionType.INCOME,
                icon = Icons.Default.Work
            )
        )

        // Add a new expense of 100.0
        viewModel.addTransaction(
            TransactionItem(
                title = "Dinner",
                category = "Food & Dining",
                paymentMethod = "Cash",
                amount = 100.0,
                type = TransactionType.EXPENSE,
                icon = Icons.Default.Restaurant
            )
        )

        val updatedSummary = viewModel.uiState.value.financeSummary
        assertEquals(1500.0, updatedSummary.totalIncome, 0.01)
        assertEquals(100.0, updatedSummary.totalExpense, 0.01)
        assertEquals(1400.0, updatedSummary.totalBalance, 0.01)
        assertEquals("Dinner", updatedSummary.recentTransactions.first().title)
    }

    @Test
    fun `MainViewModel correctly computes budget usage percentage`() {
        val viewModel = MainViewModel()
        val summary = viewModel.uiState.value.financeSummary

        // 0 / 3000 = 0.0
        assertEquals(0.0f, summary.budgetUsagePercentage, 0.001f)
    }
 }
