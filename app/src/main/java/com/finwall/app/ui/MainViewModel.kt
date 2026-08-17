package com.finwall.app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsSubway
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.lifecycle.ViewModel
import com.finwall.app.data.model.BudgetItem
import com.finwall.app.data.model.CategoryOption
import com.finwall.app.data.model.DefaultDebtCategories
import com.finwall.app.data.model.DefaultExpenseCategories
import com.finwall.app.data.model.DefaultIncomeCategories
import com.finwall.app.data.model.DefaultLentCategories
import com.finwall.app.data.model.FinanceSummary
import com.finwall.app.data.model.FinancialActivityLog
import com.finwall.app.data.model.TransactionDateGroup
import com.finwall.app.data.model.TransactionItem
import com.finwall.app.data.model.TransactionType
import com.finwall.app.data.model.formatTimestampToDate
import com.finwall.app.navigation.Screen
import com.finwall.app.ui.theme.MonetSeedColor
import com.finwall.app.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

data class MainUiState(
    val currentScreen: Screen = Screen.Home,
    val previousScreenIndex: Int = 0,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isDynamicMonetEnabled: Boolean = false,
    val selectedSeedColor: MonetSeedColor = MonetSeedColor.PURPLE,
    val isBouncyMotion: Boolean = true,
    val transactions: List<TransactionItem> = emptyList(),
    val categoriesMap: Map<TransactionType, List<CategoryOption>> = emptyMap(),
    val budgets: List<BudgetItem> = emptyList(),
    val monthlyBudgetLimit: Double = 3000.0,
    val activityLogs: List<FinancialActivityLog> = emptyList()
) {
    val financeSummary: FinanceSummary
        get() {
            var income = 0.0
            var expense = 0.0
            var lent = 0.0
            var debt = 0.0

            transactions.forEach { item ->
                when (item.type) {
                    TransactionType.INCOME -> income += item.amount
                    TransactionType.EXPENSE -> expense += item.amount
                    TransactionType.LENT -> lent += item.amount
                    TransactionType.DEBT -> debt += item.amount
                }
            }

            val totalBalance = income - expense + lent - debt
            val usagePct = if (monthlyBudgetLimit > 0) {
                (expense / monthlyBudgetLimit).toFloat().coerceIn(0f, 1f)
            } else 0f

            // Calculate category spending proportions for top visualizer
            val expensesByCategory = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }

            val maxCategorySpend = expensesByCategory.values.maxOrNull() ?: 1.0
            val proportions = if (expensesByCategory.isNotEmpty()) {
                expensesByCategory.values.map { (it / maxCategorySpend).toFloat().coerceIn(0.2f, 1.0f) }
            } else {
                listOf(0.4f, 0.7f, 0.55f, 0.9f, 0.65f, 0.8f, 0.95f)
            }

            return FinanceSummary(
                totalBalance = totalBalance,
                totalIncome = income,
                totalExpense = expense,
                totalLent = lent,
                totalDebt = debt,
                monthlyBudgetLimit = monthlyBudgetLimit,
                budgetUsagePercentage = usagePct,
                recentTransactions = transactions.take(4),
                categorySpendProportions = proportions
            )
        }

    val groupedTransactions: List<TransactionDateGroup>
        get() {
            return transactions
                .groupBy { it.formattedDate }
                .map { (dateLabel, items) -> TransactionDateGroup(dateLabel, items) }
        }
}

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(createInitialUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private fun createInitialUiState(): MainUiState {
        val starterTransactions = emptyList<TransactionItem>()
        val starterLogs = emptyList<FinancialActivityLog>()

        val categoriesMap = mapOf(
            TransactionType.EXPENSE to DefaultExpenseCategories,
            TransactionType.INCOME to DefaultIncomeCategories,
            TransactionType.LENT to DefaultLentCategories,
            TransactionType.DEBT to DefaultDebtCategories
        )

        return MainUiState(
            transactions = starterTransactions,
            categoriesMap = categoriesMap,
            monthlyBudgetLimit = 3000.0,
            activityLogs = starterLogs
        )
    }

    fun navigateTo(screen: Screen) {
        _uiState.update { currentState ->
            if (currentState.currentScreen == screen) currentState
            else currentState.copy(
                previousScreenIndex = currentState.currentScreen.index,
                currentScreen = screen
            )
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _uiState.update { it.copy(themeMode = mode) }
    }

    fun setDynamicMonetEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isDynamicMonetEnabled = enabled) }
    }

    fun setSeedColor(seed: MonetSeedColor) {
        _uiState.update { it.copy(selectedSeedColor = seed) }
    }

    fun toggleBouncyMotion() {
        _uiState.update { it.copy(isBouncyMotion = !it.isBouncyMotion) }
    }

    fun addTransaction(transaction: TransactionItem) {
        _uiState.update { current ->
            val updatedList = listOf(transaction) + current.transactions
            val newLog = FinancialActivityLog(
                title = when (transaction.type) {
                    TransactionType.EXPENSE -> "Expense Logged: ${transaction.title}"
                    TransactionType.INCOME -> "Income Received: ${transaction.title}"
                    TransactionType.LENT -> "Money Lent: ${transaction.title}"
                    TransactionType.DEBT -> "Debt Borrowed: ${transaction.title}"
                },
                subtitle = "${transaction.displayAmount} via ${transaction.paymentMethod} • ${transaction.category}",
                icon = transaction.icon,
                isHighlighted = transaction.type == TransactionType.INCOME,
                timestamp = transaction.timestamp
            )
            current.copy(
                transactions = updatedList,
                activityLogs = listOf(newLog) + current.activityLogs
            )
        }
    }

    fun updateTransaction(transaction: TransactionItem) {
        _uiState.update { current ->
            val updatedList = current.transactions.map { if (it.id == transaction.id) transaction else it }
            val editLog = FinancialActivityLog(
                title = "Transaction Updated: ${transaction.title}",
                subtitle = "${transaction.displayAmount} via ${transaction.paymentMethod} • ${transaction.category}",
                icon = transaction.icon,
                isHighlighted = false,
                timestamp = System.currentTimeMillis()
            )
            current.copy(
                transactions = updatedList,
                activityLogs = listOf(editLog) + current.activityLogs
            )
        }
    }

    fun deleteTransaction(transactionId: String) {
        _uiState.update { current ->
            val target = current.transactions.find { it.id == transactionId }
            val updatedList = current.transactions.filter { it.id != transactionId }
            val deleteLog = target?.let {
                FinancialActivityLog(
                    title = "Transaction Deleted: ${it.title}",
                    subtitle = "${it.displayAmount} removed",
                    icon = it.icon,
                    isHighlighted = false,
                    timestamp = System.currentTimeMillis()
                )
            }
            current.copy(
                transactions = updatedList,
                activityLogs = if (deleteLog != null) listOf(deleteLog) + current.activityLogs else current.activityLogs
            )
        }
    }

    fun addCustomCategory(type: TransactionType, category: CategoryOption) {
        _uiState.update { current ->
            val existing = current.categoriesMap[type] ?: emptyList()
            val updated = listOf(category) + existing
            val newMap = current.categoriesMap.toMutableMap().apply { put(type, updated) }
            current.copy(categoriesMap = newMap)
        }
    }

    fun addBudget(budget: BudgetItem) {
        _uiState.update { current ->
            val updatedList = listOf(budget) + current.budgets
            val newLog = FinancialActivityLog(
                title = "Budget Created: ${budget.name}",
                subtitle = "Limit: AED ${"%,.2f".format(budget.monthlyLimit)} • ${budget.category}",
                icon = budget.icon,
                isHighlighted = true,
                timestamp = System.currentTimeMillis()
            )
            current.copy(
                budgets = updatedList,
                activityLogs = listOf(newLog) + current.activityLogs
            )
        }
    }

    fun updateBudget(budget: BudgetItem) {
        _uiState.update { current ->
            val updatedList = current.budgets.map { if (it.id == budget.id) budget else it }
            val editLog = FinancialActivityLog(
                title = "Budget Updated: ${budget.name}",
                subtitle = "Limit: AED ${"%,.2f".format(budget.monthlyLimit)} • ${budget.category}",
                icon = budget.icon,
                isHighlighted = false,
                timestamp = System.currentTimeMillis()
            )
            current.copy(
                budgets = updatedList,
                activityLogs = listOf(editLog) + current.activityLogs
            )
        }
    }

    fun deleteBudget(budgetId: String) {
        _uiState.update { current ->
            val target = current.budgets.find { it.id == budgetId }
            val updatedList = current.budgets.filter { it.id != budgetId }
            val deleteLog = target?.let {
                FinancialActivityLog(
                    title = "Budget Deleted: ${it.name}",
                    subtitle = "${it.category} limit removed",
                    icon = it.icon,
                    isHighlighted = false,
                    timestamp = System.currentTimeMillis()
                )
            }
            current.copy(
                budgets = updatedList,
                activityLogs = if (deleteLog != null) listOf(deleteLog) + current.activityLogs else current.activityLogs
            )
        }
    }

    fun setMonthlyBudget(limit: Double) {
        _uiState.update { it.copy(monthlyBudgetLimit = limit) }
    }
}
