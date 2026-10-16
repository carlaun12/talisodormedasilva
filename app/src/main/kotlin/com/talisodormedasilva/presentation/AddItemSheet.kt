package com.talisodormedasilva.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.talisodormedasilva.R
import com.talisodormedasilva.domain.FinancialDirection
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

internal val EntryDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)

// Accept the Brazilian decimal separator, with no grouping or rounding of cents.
internal fun parseAmountCents(value: String): Long? {
    val normalized = value.trim().replace(',', '.')
    if (!Regex("[0-9]+(?:\\.[0-9]{1,2})?").matches(normalized)) return null
    return runCatching { BigDecimal(normalized).movePointRight(2).longValueExact() }.getOrNull()?.takeIf { it > 0 }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddItemSheet(
    initialDate: LocalDate,
    isSaving: Boolean,
    saveError: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, FinancialDirection, Long, LocalDate) -> Unit,
) {
    var label by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf(initialDate.format(EntryDateFormat)) }
    var inflow by rememberSaveable { mutableStateOf(false) }
    var submitted by rememberSaveable { mutableStateOf(false) }
    val cents = parseAmountCents(amount)
    val parsedDate = runCatching { LocalDate.parse(date, EntryDateFormat) }.getOrNull()
    val labelValid = label.trim().isNotEmpty() && label.trim().length <= 90
    val saving by rememberUpdatedState(isSaving)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true,
        confirmValueChange = { !saving || it != SheetValue.Hidden })
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState, containerColor = CalendarBackground) {
        Column(Modifier.fillMaxWidth().imePadding().verticalScroll(rememberScrollState()).padding(start = 24.dp, end = 24.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(stringResource(R.string.new_item), style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(label, { label = it.take(90) }, modifier = Modifier.fillMaxWidth().testTag("entry-description"),
                label = { Text(stringResource(R.string.description)) }, singleLine = true, enabled = !isSaving,
                isError = submitted && !labelValid,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences))
            Text(stringResource(R.string.item_type), style = MaterialTheme.typography.labelLarge)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(selected = !inflow, onClick = { inflow = false }, enabled = !isSaving,
                    label = { Text(stringResource(R.string.expected_outflow)) })
                FilterChip(selected = inflow, onClick = { inflow = true }, enabled = !isSaving,
                    label = { Text(stringResource(R.string.expected_inflow)) }, modifier = Modifier.testTag("entry-inflow"))
            }
            OutlinedTextField(amount, { amount = it }, modifier = Modifier.fillMaxWidth().testTag("entry-amount"),
                label = { Text(stringResource(R.string.amount)) }, placeholder = { Text("0,00") },
                singleLine = true, enabled = !isSaving, isError = submitted && cents == null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
            OutlinedTextField(date, { date = it }, modifier = Modifier.fillMaxWidth().testTag("entry-date"),
                label = { Text(stringResource(R.string.item_date)) }, placeholder = { Text("dd/mm/aaaa") },
                singleLine = true, enabled = !isSaving, isError = submitted && parsedDate == null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))
            if (submitted && (!labelValid || cents == null || parsedDate == null)) {
                Text(stringResource(R.string.invalid_entry), color = MaterialTheme.colorScheme.error)
            }
            if (saveError) Text(stringResource(R.string.save_error), color = MaterialTheme.colorScheme.error)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilledTonalButton(onClick = onDismiss, enabled = !isSaving, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.cancel))
                }
                Button(onClick = {
                    submitted = true
                    if (labelValid && cents != null && parsedDate != null) {
                        onSave(label.trim(), if (inflow) FinancialDirection.INFLOW else FinancialDirection.OUTFLOW, cents, parsedDate)
                    }
                }, enabled = !isSaving, modifier = Modifier.weight(1f).testTag("save-item")) {
                    Text(stringResource(if (isSaving) R.string.saving else R.string.save))
                }
            }
        }
    }
}
