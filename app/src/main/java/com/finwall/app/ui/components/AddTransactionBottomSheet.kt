package com.finwall.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import com.finwall.app.data.model.CategoryOption
import com.finwall.app.data.model.DefaultDebtCategories
import com.finwall.app.data.model.DefaultExpenseCategories
import com.finwall.app.data.model.DefaultIncomeCategories
import com.finwall.app.data.model.DefaultLentCategories
import com.finwall.app.data.model.TransactionItem
import com.finwall.app.data.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    transactionToEdit: TransactionItem? = null,
    categoriesMap: Map<TransactionType, List<CategoryOption>> = emptyMap(),
    onAddCustomCategory: ((TransactionType, CategoryOption) -> Unit)? = null,
    onDismiss: () -> Unit,
    onSaveTransaction: (TransactionItem) -> Unit
) {
    var selectedType by remember(transactionToEdit) { mutableStateOf(transactionToEdit?.type ?: TransactionType.EXPENSE) }
    var amountText by remember(transactionToEdit) {
        mutableStateOf(
            transactionToEdit?.let {
                if (it.amount == it.amount.toLong().toDouble()) it.amount.toLong().toString()
                else String.format(Locale.US, "%.2f", it.amount)
            } ?: ""
        )
    }
    var titleText by remember(transactionToEdit) { mutableStateOf(transactionToEdit?.title ?: "") }
    var selectedPaymentMethod by remember(transactionToEdit) { mutableStateOf(transactionToEdit?.paymentMethod ?: "Cash") }
    var isSavePressed by remember { mutableStateOf(false) }
    var showCategoryPickerSheet by remember { mutableStateOf(false) }

    // Dynamic Category lists per type (fallback to defaults if empty)
    val activeCategoriesMap = remember(categoriesMap) {
        if (categoriesMap.isNotEmpty()) categoriesMap
        else mapOf(
            TransactionType.EXPENSE to DefaultExpenseCategories,
            TransactionType.INCOME to DefaultIncomeCategories,
            TransactionType.LENT to DefaultLentCategories,
            TransactionType.DEBT to DefaultDebtCategories
        )
    }

    val currentCategories = activeCategoriesMap[selectedType] ?: DefaultExpenseCategories
    var selectedCategory by remember(selectedType, currentCategories, transactionToEdit) {
        mutableStateOf(
            if (transactionToEdit != null && transactionToEdit.type == selectedType) {
                currentCategories.find { it.name == transactionToEdit.category } ?: CategoryOption(transactionToEdit.category, transactionToEdit.icon, transactionToEdit.type)
            } else {
                currentCategories.first()
            }
        )
    }

    val paymentMethods = listOf(
        Pair("Cash", Icons.Default.LocalAtm),
        Pair("Bank", Icons.Default.Payment),
        Pair("Card", Icons.Default.CreditCard)
    )

    // Dynamic accent colors based on transaction type
    val typeColor = when (selectedType) {
        TransactionType.EXPENSE -> MaterialTheme.colorScheme.error
        TransactionType.INCOME -> MaterialTheme.colorScheme.primary
        TransactionType.LENT -> MaterialTheme.colorScheme.secondary
        TransactionType.DEBT -> MaterialTheme.colorScheme.tertiary
    }

    val typeContainerColor = when (selectedType) {
        TransactionType.EXPENSE -> MaterialTheme.colorScheme.errorContainer
        TransactionType.INCOME -> MaterialTheme.colorScheme.primaryContainer
        TransactionType.LENT -> MaterialTheme.colorScheme.secondaryContainer
        TransactionType.DEBT -> MaterialTheme.colorScheme.tertiaryContainer
    }

    val onTypeContainerColor = when (selectedType) {
        TransactionType.EXPENSE -> MaterialTheme.colorScheme.onErrorContainer
        TransactionType.INCOME -> MaterialTheme.colorScheme.onPrimaryContainer
        TransactionType.LENT -> MaterialTheme.colorScheme.onSecondaryContainer
        TransactionType.DEBT -> MaterialTheme.colorScheme.onTertiaryContainer
    }

    val saveButtonScale by animateFloatAsState(
        targetValue = if (isSavePressed) 0.96f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "save_button_scale"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        dragHandle = {
            Surface(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 36.dp, height = 4.dp),
                shape = RoundedCornerShape(2.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)
            ) {}
        }
    ) {
        // Uniform Vertical Layout guaranteeing constant height across Expense, Income, Lent, and Debt
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Bar: Title + Close Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (transactionToEdit != null) "Edit Transaction" else "New Transaction",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (transactionToEdit != null) "Update amount, category, or payment details" else "Record an income, expense, lent or debt",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.size(36.dp)
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // 1. Transaction Type Segmented Switcher Row (Zero Text Wrap)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TransactionType.values().forEach { type ->
                    val isSelected = selectedType == type
                    val tabShape = RoundedCornerShape(14.dp)

                    val activeBgColor by animateColorAsState(
                        targetValue = if (isSelected) when (type) {
                            TransactionType.EXPENSE -> MaterialTheme.colorScheme.error
                            TransactionType.INCOME -> MaterialTheme.colorScheme.primary
                            TransactionType.LENT -> MaterialTheme.colorScheme.secondary
                            TransactionType.DEBT -> MaterialTheme.colorScheme.tertiary
                        } else Color.Transparent,
                        animationSpec = spring(stiffness = Spring.StiffnessLow),
                        label = "tab_bg_${type.name}"
                    )

                    val activeContentColor by animateColorAsState(
                        targetValue = if (isSelected) when (type) {
                            TransactionType.EXPENSE -> MaterialTheme.colorScheme.onError
                            TransactionType.INCOME -> MaterialTheme.colorScheme.onPrimary
                            TransactionType.LENT -> MaterialTheme.colorScheme.onSecondary
                            TransactionType.DEBT -> MaterialTheme.colorScheme.onTertiary
                        } else MaterialTheme.colorScheme.onSurfaceVariant,
                        animationSpec = spring(stiffness = Spring.StiffnessLow),
                        label = "tab_text_${type.name}"
                    )

                    val typeLabel = when (type) {
                        TransactionType.EXPENSE -> "Expense"
                        TransactionType.INCOME -> "Income"
                        TransactionType.LENT -> "Lent"
                        TransactionType.DEBT -> "Debt"
                    }

                    val typeIcon = when (type) {
                        TransactionType.EXPENSE -> Icons.Default.ArrowDownward
                        TransactionType.INCOME -> Icons.Default.ArrowUpward
                        TransactionType.LENT -> Icons.AutoMirrored.Filled.CallMade
                        TransactionType.DEBT -> Icons.AutoMirrored.Filled.CallReceived
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(tabShape)
                            .background(activeBgColor)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                                )
                            ) {
                                selectedType = type
                                selectedCategory = (activeCategoriesMap[type] ?: DefaultExpenseCategories).first()
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = typeIcon,
                                contentDescription = null,
                                tint = activeContentColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = typeLabel,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = activeContentColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // 2. Hero Amount Input Card (Large Display Typography)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = typeContainerColor.copy(alpha = 0.35f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Enter Amount (AED)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = typeColor
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = when (selectedType) {
                                TransactionType.EXPENSE -> "- AED "
                                TransactionType.INCOME -> "+ AED "
                                TransactionType.LENT -> "AED "
                                TransactionType.DEBT -> "AED "
                            },
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = typeColor
                        )

                        OutlinedTextField(
                            value = amountText,
                            onValueChange = { input ->
                                if (input.isEmpty() || input.matches(Regex("""^\d*\.?\d{0,2}$"""))) {
                                    amountText = input
                                }
                            },
                            placeholder = {
                                Text(
                                    text = "0.00",
                                    style = MaterialTheme.typography.displaySmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = onTypeContainerColor.copy(alpha = 0.4f)
                                )
                            },
                            textStyle = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = onTypeContainerColor,
                                textAlign = TextAlign.Start
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier.width(180.dp)
                        )
                    }
                }
            }

            // 3. Compact Material 3 Expressive Category Selector Tile
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    onClick = { showCategoryPickerSheet = true },
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Circular Tonal Category Badge
                        Surface(
                            shape = CircleShape,
                            color = typeContainerColor,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = selectedCategory.icon,
                                    contentDescription = null,
                                    tint = onTypeContainerColor,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // Category Label + Tap to Browse Subtitle
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedCategory.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Tap to search or create category",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Trailing Chevron Indicator
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Select category",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 4. Details OutlinedCard (Title, Note, Payment Method)
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Transaction Details",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Note / Title Field
                    OutlinedTextField(
                        value = titleText,
                        onValueChange = { titleText = it },
                        label = { Text("Title / Description (optional)") },
                        placeholder = { Text(selectedCategory.name) },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // Payment Method Row
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Payment Method",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            paymentMethods.forEach { (method, icon) ->
                                val isSelected = selectedPaymentMethod == method
                                val shape = RoundedCornerShape(14.dp)

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(shape)
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                                            else MaterialTheme.colorScheme.surfaceContainerLow
                                        )
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = ripple(color = MaterialTheme.colorScheme.primary)
                                        ) { selectedPaymentMethod = method }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                                            else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = method,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            ),
                                            color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                                            else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. Primary Save Transaction Pill Button
            val isFormValid = amountText.isNotEmpty() && (amountText.toDoubleOrNull() ?: 0.0) > 0

            Surface(
                onClick = {
                    if (isFormValid) {
                        val finalTitle = if (titleText.isNotBlank()) titleText.trim() else selectedCategory.name
                        val rawAmount = amountText.toDoubleOrNull() ?: 0.0

                        val finalTransaction = if (transactionToEdit != null) {
                            TransactionItem(
                                id = transactionToEdit.id,
                                title = finalTitle,
                                category = selectedCategory.name,
                                paymentMethod = selectedPaymentMethod,
                                amount = rawAmount,
                                type = selectedType,
                                icon = selectedCategory.icon,
                                timestamp = transactionToEdit.timestamp
                            )
                        } else {
                            TransactionItem(
                                title = finalTitle,
                                category = selectedCategory.name,
                                paymentMethod = selectedPaymentMethod,
                                amount = rawAmount,
                                type = selectedType,
                                icon = selectedCategory.icon,
                                timestamp = System.currentTimeMillis()
                            )
                        }

                        onSaveTransaction(finalTransaction)
                    }
                },
                enabled = isFormValid,
                shape = RoundedCornerShape(28.dp),
                color = if (isFormValid) typeColor else MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = if (isFormValid) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .scale(saveButtonScale)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (transactionToEdit != null) "Update Transaction" else "Save Transaction",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Slide-up Category Picker Modal Sheet with Search & Custom Category Creator
        if (showCategoryPickerSheet) {
            CategoryPickerBottomSheet(
                transactionType = selectedType,
                categories = currentCategories,
                selectedCategory = selectedCategory,
                onCategorySelected = { chosenCat ->
                    selectedCategory = chosenCat
                    showCategoryPickerSheet = false
                },
                onAddCustomCategory = { newCustomCat ->
                    onAddCustomCategory?.invoke(selectedType, newCustomCat)
                    selectedCategory = newCustomCat
                    showCategoryPickerSheet = false
                },
                onDismiss = { showCategoryPickerSheet = false }
            )
        }
    }
}
