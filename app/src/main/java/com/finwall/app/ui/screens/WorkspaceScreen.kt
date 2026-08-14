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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsSubway
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Data Model for Transaction Items
 */
data class TransactionItem(
    val title: String,
    val category: String,
    val paymentMethod: String,
    val time: String,
    val amount: String,
    val type: TransactionType,
    val icon: ImageVector
)

enum class TransactionType {
    EXPENSE, INCOME, LENT, DEBT
}

data class TransactionDateGroup(
    val dateLabel: String,
    val items: List<TransactionItem>
)

/**
 * Material 3 Expressive Transactions Screen Layout Schema
 *
 * Implements category filter pills, date dropdown, view toggle, shadowless OutlinedCards,
 * avatar badges, amount indicators, and floating action button.
 */
@Composable
fun WorkspaceScreen(
    innerPadding: PaddingValues
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var isGridView by remember { mutableStateOf(false) }

    val categories = listOf("All", "Expense", "Income", "Lent", "Debt")

    val transactionGroups = listOf(
        TransactionDateGroup(
            dateLabel = "Today",
            items = listOf(
                TransactionItem(
                    title = "Lunch",
                    category = "Food",
                    paymentMethod = "Cash",
                    time = "10:15 AM",
                    amount = "- AED 25.00",
                    type = TransactionType.EXPENSE,
                    icon = Icons.Default.Restaurant
                ),
                TransactionItem(
                    title = "Rent",
                    category = "Home",
                    paymentMethod = "Bank",
                    time = "9:00 AM",
                    amount = "- AED 500.00",
                    type = TransactionType.EXPENSE,
                    icon = Icons.Default.Home
                ),
                TransactionItem(
                    title = "Salary",
                    category = "Salary",
                    paymentMethod = "Bank",
                    time = "8:30 AM",
                    amount = "+ AED 3,200.00",
                    type = TransactionType.INCOME,
                    icon = Icons.Default.Work
                )
            )
        ),
        TransactionDateGroup(
            dateLabel = "Yesterday",
            items = listOf(
                TransactionItem(
                    title = "Groceries",
                    category = "Food",
                    paymentMethod = "Cash",
                    time = "Yesterday, 6:45 PM",
                    amount = "- AED 65.00",
                    type = TransactionType.EXPENSE,
                    icon = Icons.Default.ShoppingCart
                ),
                TransactionItem(
                    title = "Metro Card Recharge",
                    category = "Travel",
                    paymentMethod = "Bank",
                    time = "Yesterday, 8:10 AM",
                    amount = "- AED 20.00",
                    type = TransactionType.EXPENSE,
                    icon = Icons.Default.DirectionsSubway
                )
            )
        ),
        TransactionDateGroup(
            dateLabel = "Aug 9, 2026",
            items = listOf(
                TransactionItem(
                    title = "Trip to Dubai",
                    category = "Travel",
                    paymentMethod = "Cash",
                    time = "Aug 9, 6:20 PM",
                    amount = "- AED 120.00",
                    type = TransactionType.EXPENSE,
                    icon = Icons.Default.Flight
                ),
                TransactionItem(
                    title = "Lent to Ahmed",
                    category = "Lending",
                    paymentMethod = "Cash",
                    time = "Aug 9, 2:30 PM",
                    amount = "- AED 200.00",
                    type = TransactionType.LENT,
                    icon = Icons.Default.Person
                ),
                TransactionItem(
                    title = "Freelance Payment",
                    category = "Freelance",
                    paymentMethod = "Bank",
                    time = "Aug 9, 11:00 AM",
                    amount = "+ AED 750.00",
                    type = TransactionType.INCOME,
                    icon = Icons.Default.CardGiftcard
                )
            )
        ),
        TransactionDateGroup(
            dateLabel = "Aug 8, 2026",
            items = listOf(
                TransactionItem(
                    title = "Movie Tickets",
                    category = "Entertainment",
                    paymentMethod = "Cash",
                    time = "Aug 8, 8:15 PM",
                    amount = "- AED 45.00",
                    type = TransactionType.EXPENSE,
                    icon = Icons.Default.Movie
                )
            )
        )
    )

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
            itemsIndexed(transactionGroups) { _, group ->
                TransactionDateGroupCard(group = group)
            }

            // Bottom Spacing for Navigation Bar & FAB
            item {
                Spacer(modifier = Modifier.height(84.dp))
            }
        }

        // Floating Action Button (FAB) anchored at bottom right
        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 96.dp, end = 20.dp),
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Transaction",
                modifier = Modifier.size(26.dp)
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
                    Icons.Default.ReceiptLong
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
                    text = "August 2026",
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
                        imageVector = Icons.Default.FormatListBulleted,
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
    group: TransactionDateGroup
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
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
            )
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                group.items.forEachIndexed { index, item ->
                    TransactionItemRow(item = item)
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
    item: TransactionItem
) {
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
                    text = item.time,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                )
            }
        }

        // Trailing Amount, Type Pill Badge, & Overflow Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.amount,
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

            IconButton(
                onClick = { },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
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
