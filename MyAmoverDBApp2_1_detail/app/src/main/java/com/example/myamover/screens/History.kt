package com.example.myamover.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myamover.data.remote.TaskRemote
import com.example.myamover.model.TaskRemoteViewModel
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * HISTORY SCREEN
 * - Lista tarefas ordenadas (por prazo ou criação)
 * - Seletor de intervalo de datas
 * - Filtro por campo de data (Criação vs Prazo)
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onOpenMapRoute: (() -> Unit)? = null,
    vm: TaskRemoteViewModel = viewModel(),
    navigationIcon: @Composable () -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val uiState by vm.ui.collectAsState()
    val context = LocalContext.current

    // Estado do intervalo de datas
    var showDateDialog by remember { mutableStateOf(false) }
    var startDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
    var endDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }

    // Critério de ordenação e de filtro
    var orderBy by rememberSaveable { mutableStateOf(OrderBy.DEADLINE) }
    var filterBy by rememberSaveable { mutableStateOf(FilterBy.DEADLINE) }
    var expandedId by rememberSaveable { mutableStateOf<Int?>(null) }

    // Lista tratada (ordenada + filtrada)
    val tasksProcessed = remember(uiState.tasks, startDate, endDate, orderBy, filterBy) {
        uiState.tasks
            .filter { task ->
                if (startDate == null && endDate == null) return@filter true
                val date = when (filterBy) {
                    FilterBy.DEADLINE -> task.deadline?.toLocalDateOrNull()
                    FilterBy.CREATED -> task.creationDate?.toLocalDateOrNull()
                }
                val s = startDate ?: LocalDate.MIN
                val e = endDate ?: LocalDate.MAX
                if (date != null) (date >= s && date <= e) else false
            }
            .sortedWith(
                when (orderBy) {
                    OrderBy.DEADLINE -> compareBy(nullsLast(LocalDate::compareTo)) { it.deadline?.toLocalDateOrNull() }
                    OrderBy.CREATED -> compareBy(nullsLast(LocalDate::compareTo)) { it.creationDate?.toLocalDateOrNull() }
                    OrderBy.STATUS -> compareBy(nullsLast(String.CASE_INSENSITIVE_ORDER)) { it.status }
                }
            )
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .height(60.dp),
                windowInsets = windowInsets,
                scrollBehavior = scrollBehavior,
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()      // ocupa toda a altura do topbar
                            .fillMaxWidth(),      // permite centrar horizontal se quiseres também
                        contentAlignment = Alignment.TopStart   // ou Center
                    ) {
                        Text(
                            text = "History",
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                        },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Botão calendário
                    IconButton(onClick = { showDateDialog = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Escolher datas")
                    }
                    // Menu ordenação/filtro
                    HistoryMenus(
                        orderBy = orderBy,
                        onOrderChange = { orderBy = it },
                        filterBy = filterBy,
                        onFilterChange = { filterBy = it }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            )
        }
    ) { padding ->

        when {
            uiState.loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding), contentAlignment = Alignment.Center
                ) {
                    Text("Erro: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                }
            }

            uiState.tasks.isEmpty() -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding), contentAlignment = Alignment.Center
                ) {
                    Text("Sem tarefas.")
                }
            }

            else -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = padding.calculateTopPadding())
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Cabeçalho com datas selecionadas
                    SelectedDatesHeader(startDate, endDate, onClear = {
                        startDate = null; endDate = null
                    })

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(uiState.tasks, key = { it.id }) { task ->
                            val expanded = expandedId?.toInt() == task.id
                            HistoryTaskCard(
                                task = task ,
                                expanded = expanded,
                                onToggleExpand = {
                                    expandedId = if (expanded) null else task.id
                                },
                                onViewTaskDetails = { /* abre detalhes */ },
                                onBackClick = { /* volta */ }
                            )
                        }
                    }

                }
            }
        }
    }

    if (showDateDialog) {
        DateRangePickerDialog(
            onDismissRequest = { showDateDialog = false },
            onConfirm = { s, e ->
                startDate = s
                endDate = e
                showDateDialog = false
            }
        )
    }
}

// -- Menus de ordenação e filtro ----------------------------------------------------

enum class OrderBy { DEADLINE, CREATED, STATUS }
enum class FilterBy { DEADLINE, CREATED }

@Composable
private fun HistoryMenus(
    orderBy: OrderBy,
    onOrderChange: (OrderBy) -> Unit,
    filterBy: FilterBy,
    onFilterChange: (FilterBy) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.FilterList, contentDescription = "Ordenar/Filtrar")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Text(
                "Ordenar por",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )
            DropdownMenuItem(
                text = { Text("Prazo") },
                onClick = { onOrderChange(OrderBy.DEADLINE); expanded = false })
            DropdownMenuItem(
                text = { Text("Criação") },
                onClick = { onOrderChange(OrderBy.CREATED); expanded = false })
            DropdownMenuItem(
                text = { Text("Estado") },
                onClick = { onOrderChange(OrderBy.STATUS); expanded = false })
            Divider()
            Text(
                "Filtrar pela data de…",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )
            DropdownMenuItem(
                text = { Text("Prazo") },
                onClick = { onFilterChange(FilterBy.DEADLINE); expanded = false })
            DropdownMenuItem(
                text = { Text("Criação") },
                onClick = { onFilterChange(FilterBy.CREATED); expanded = false })
        }
    }
}

// -- Cabeçalho com datas selecionadas ----------------------------------------------

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun SelectedDatesHeader(start: LocalDate?, end: LocalDate?, onClear: () -> Unit) {
    val label = when {
        start == null && end == null -> "Todas as datas"
        start != null && end != null -> "${start.formatPt()} → ${end.formatPt()}"
        start != null -> "Desde ${start.formatPt()}"
        else -> "Até ${end!!.formatPt()}"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AssistChip(onClick = {}, label = { Text(label) })
        Spacer(Modifier.width(8.dp))
        if (start != null || end != null) {
            TextButton(onClick = onClear) { Text("Limpar") }
        }
    }
}

// -- Card de tarefa para histórico --------------------------------------------------

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistoryTaskCard(
    task: TaskRemote,
    onViewTaskDetails: () -> Unit,
    onBackClick: () -> Unit,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .animateContentSize()
        .clickable (onClick = onToggleExpand),
        shape = RoundedCornerShape(13.dp),
        ) {
        Column(Modifier.padding(16.dp)) {
            // topo do card (sempre visível)
            Text("${task.type ?: "—"} • ${task.status ?: "—"}", fontWeight = FontWeight.Bold)
            if (!task.description.isNullOrBlank()) Text(task.description!!)
            Spacer(Modifier.height(8.dp))
            Text("Created in: ${task.creationDate ?: "—"}")
            Text("Term: ${task.deadline ?: "—"}")
            // conteúdo extra só quando expandido (ícone + botão "View Details")

            AnimatedVisibility(visible = expanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // exemplo de ícone/ação extra à esquerda
//                    IconButton(onClick = { /* outra ação */ }) {
//                        Icon(Icons.Default.Map, contentDescription = "Open map")
//                    }
                    Button(onClick = onViewTaskDetails) {
                        Text("View Details")
                    }
                }
            }
        }
    }
}

// -- DateRangePickerDialog ----------------------------------------------------------

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (start: LocalDate?, end: LocalDate?) -> Unit,
) {
    val state = rememberDateRangePickerState()
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    val s = state.selectedStartDateMillis?.toLocalDate()
                    val e = state.selectedEndDateMillis?.toLocalDate()
                    onConfirm(s, e)
                }
            ) { Text("Aplicar") }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text("Cancelar") }
        }
    ) {
        DateRangePicker(state = state, title = { Text("Escolher intervalo de datas") })
    }
}


// -- Utils de datas -----------------------------------------------------------------

@RequiresApi(Build.VERSION_CODES.O)
private fun String.toLocalDateOrNull(): LocalDate? =
    runCatching {
        // tenta ISO OffsetDateTime ou LocalDate
        when {
            contains("T", ignoreCase = false) -> OffsetDateTime.parse(this).toLocalDate()
            else -> LocalDate.parse(this)
        }
    }.getOrNull()

@RequiresApi(Build.VERSION_CODES.O)
private fun Long.toLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate =
    java.time.Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate()

@RequiresApi(Build.VERSION_CODES.O)
private fun LocalDate.formatPt(): String =
    this.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))