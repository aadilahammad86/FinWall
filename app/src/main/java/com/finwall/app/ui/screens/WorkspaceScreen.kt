package com.finwall.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finwall.app.data.model.CategoryOption
import com.finwall.app.data.model.TransactionDateGroup
import com.finwall.app.data.model.TransactionItem
import com.finwall.app.data.model.TransactionType
import com.finwall.app.ui.components.AddTransactionBottomSheet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Material 3 Expressive Transactions Screen Layout Schema
 *
 * Implements category filter pills, dynamic date dropdown, view mode toggle,
 * shadowless OutlinedCards, avatar badges, amount indicators, and floating action button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceScreen(
    innerPadding: PaddingValues,
    transactions: List<TransactionItem> = emptyList(),
    categoriesMap: Map<TransactionType, List<CategoryOption>> = emptyMap(),
    onAddTransaction: (TransactionItem) -> Unit = {},
    onUpdateTransaction: (TransactionItem) -> Unit = {},
    onDeleteTransaction: (String) -> Unit = {},
    onAddCustomCategory: ((TransactionType, CategoryOption) -> Unit)? = null
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var isGridView by remember { mutableStateOf(false) }
    var showAddTransactionSheet by remember { mutableStateOf(false) }
    var editingTransaction by remember { mutableStateOf<TransactionItem?>(null) }
    var transactionToDelete by remember { mutableStateOf<TransactionItem?>(null) }

    val categories = listOf("All", "Expense", "Income", "Lent", "Debt")

    // Filter displayed items by category
    val filteredTransactions = remember(selectedCategoryIndex, transactions) {
        val targetType = when (selectedCategoryIndex) {
            1 -> TransactionType.EXPENSE
            2 -> TransactionType.INCOME
            3 -> TransactionType.LENT
            4 -> TransactionType.DEBT
            else -> null
        }

        if (targetType == null) transactions
        else transactions.filter { it.type == targetType }
    }

    // Dynamic grouping by formatted date label
    val transactionGroups = remember(filteredTransactions) {
        filteredTransactions
            .groupBy { it.formattedDate }
            .map { (dateLabel, items) -> TransactionDateGroup(dateLabel, items) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Header Bar with Actions
            item {
                TransactionsHeaderBar()
            }

            // 2. Category Filter Pill Selector Row
            item {
                CategoryFilterPillRow(
                    categories = categories,
                    selectedIndex = selectedCategoryIndex,
                    onSelect = { selectedCategoryIndex = it }
                )
            }

            // 3. Date Dropdown & View Mode Toggle Bar
            item {
                DateAndLayoutControlRow(
                    isGridView = isGridView,
                    onToggleView = { isGridView = !isGridView }
                )
            }

            // 4. Grouped Transactions List Items
            if (transactionGroups.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No transactions found for ${categories[selectedCategoryIndex]}.\nTap 'Add Transaction' below to create one.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                itemsIndexed(transactionGroups) { _, group ->
                    TransactionDateGroupCard(
                        group = group,
                        onEditClick = { editingTransaction = it },
                        onDeleteClick = { transactionToDelete = it }
                    )
                }
            }

            // Bottom Spacing for Navigation Bar & FAB
            item {
                Spacer(modifier = Modifier.height(84.dp))
            }
        }

        // Material 3 Expressive Extended Floating Action Button (FAB)
        ExtendedFloatingActionButton(
            onClick = { showAddTransactionSheet = true },
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            text = {
                Text(
                    text = "Add Transaction",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            },
            shape = RoundedCornerShape(22.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 10.dp
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 96.dp, end = 20.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(22.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                )
        )

        // Material 3 Expressive Add Transaction Modal Bottom Sheet
        if (showAddTransactionSheet) {
            AddTransactionBottomSheet(
                categoriesMap = categoriesMap,
                onAddCustomCategory = onAddCustomCategory,
                onDismiss = { showAddTransactionSheet = false },
                onSaveTransaction = { newTx ->
                    onAddTransaction(newTx)
                    showAddTransactionSheet = false
                }
            )
        }

        // Material 3 Expressive Edit Transaction Modal Bottom Sheet
        if (editingTransaction != null) {
            AddTransactionBottomSheet(
                transactionToEdit = editingTransaction,
                categoriesMap = categoriesMap,
                onAddCustomCategory = onAddCustomCategory,
                onDismiss = { editingTransaction = null },
                onSaveTransaction = { updatedTx ->
                    onUpdateTransaction(updatedTx)
                    editingTransaction = null
                }
            )
        }

        // Material 3 Expressive Delete Confirmation Dialog
        if (transactionToDelete != null) {
            val item = transactionToDelete!!
            AlertDialog(
                onDismissRequest = { transactionToDelete = null },
                icon = {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                title = {
                    Text(
                        text = "Delete Transaction",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete '${item.title}' (${item.displayAmount})? This action cannot be undone.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDeleteTransaction(item.id)
                            transactionToDelete = null
                        }
                    ) {
                        Text(
                            text = "Delete",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { transactionToDelete = null }) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                shape = RoundedCornerShape(28.dp),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            )
        }
    }
}

/**
 * Top Header Bar (Title, Subtitle, Search, Filter, Overflow menu)
 */
@Composable
private fun TransactionsHeaderBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Transactions",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "All your financial activity",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Category Filter Pill Row (All, Expense, Income, Lent, Debt)
 */
@Composable
private fun CategoryFilterPillRow(
    categories: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(categories) { index, category ->
            val isSelected = selectedIndex == index

            val (containerColor, contentColor, icon) = when (category) {
                "All" -> Triple(
                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLow,
                    if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    Icons.Default.CalendarToday
                )
                "Expense" -> Triple(
                    if (isSelected) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                    if (isSelected) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.error,
                    Icons.Default.ArrowDownward
                )
                "Income" -> Triple(
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                    if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                    Icons.Default.ArrowUpward
                )
                "Lent" -> Triple(
                    if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                    if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.secondary,
                    Icons.AutoMirrored.Filled.CompareArrows
                )
                else -> Triple(
                    if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                    if (isSelected) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.tertiary,
                    Icons.AutoMirrored.Filled.ReceiptLong
                )
            }

            Surface(
                onClick = { onSelect(index) },
                shape = RoundedCornerShape(20.dp),
                color = containerColor,
                contentColor = contentColor,
                border = if (!isSelected) androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                ) else null
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = contentColor
                    )
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

/**
 * Date Dropdown AssistChip & View Mode Segmented Pill Control
 */
@Composable
private fun DateAndLayoutControlRow(
    isGridView: Boolean,
    onToggleView: () -> Unit
) {
    val monthStr = remember {
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Month / Date Dropdown AssistChip
        AssistChip(
            onClick = { },
            label = {
                Text(
                    text = monthStr,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                labelColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(20.dp)
        )

        // View Mode Segmented Pill Control
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.padding(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (!isGridView) MaterialTheme.colorScheme.primary
                            else Color.Transparent
                        )
                        .clickable { if (isGridView) onToggleView() }
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                        contentDescription = "List View",
                        tint = if (!isGridView) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isGridView) MaterialTheme.colorScheme.primary
                            else Color.Transparent
                        )
                        .clickable { if (!isGridView) onToggleView() }
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Grid View",
                        tint = if (isGridView) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Grouped Transactions Card (OutlinedCard containing transaction rows for a specific date)
 */
@Composable
private fun TransactionDateGroupCard(
    group: TransactionDateGroup,
    onEditClick: (TransactionItem) -> Unit,
    onDeleteClick: (TransactionItem) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = group.dateLabel,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(start = 4.dp)
        )

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            )
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                group.items.forEachIndexed { index, item ->
                    TransactionItemRow(
                        item = item,
                        onEditClick = onEditClick,
                        onDeleteClick = onDeleteClick
                    )
                    if (index < group.items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual Transaction Item Row
 */
@Composable
private fun TransactionItemRow(
    item: TransactionItem,
    onEditClick: (TransactionItem) -> Unit,
    onDeleteClick: (TransactionItem) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val (avatarBg, avatarIconTint, amountColor, typeBg, typeText) = when (item.type) {
        TransactionType.EXPENSE -> Tuples5(
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.errorContainer,
            "Expense"
        )
        TransactionType.INCOME -> Tuples5(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer,
            "Income"
        )
        TransactionType.LENT -> Tuples5(
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.secondaryContainer,
            "Lent"
        )
        TransactionType.DEBT -> Tuples5(
            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.tertiaryContainer,
            "Debt"
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = MaterialTheme.colorScheme.primary)
            ) { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading Avatar & Item Titles
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = CircleShape,
                color = avatarBg,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.category,
                        tint = avatarIconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "${item.category} • ${item.paymentMethod}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
                Text(
                    text = item.formattedTime,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                )
            }
        }

        // Trailing Amount, Type Pill Badge, & Overflow Button with DropdownMenu
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.displayAmount,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = amountColor
                    )
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = typeBg
                ) {
                    Text(
                        text = typeText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = amountColor,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Box {
                IconButton(
                    onClick = { menuExpanded = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Edit",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onEditClick(item)
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onDeleteClick(item)
                        }
                    )
                }
            }
        }
    }
}

private data class Tuples5<A, B, C, D, E>(
    val a: A,
    val b: B,
    val c: C,
    val d: D,
    val e: E
)
