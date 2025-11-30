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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myamover.data.remote.TaskRemote
import com.example.myamover.model.TaskRemoteViewModel
import com.example.myamover.route.utils.loadJsonFromAssets
import com.example.myamover.route.utils.startRouteFromNodesJson


/*
* val uiState by vm.ui.collectAsState() → traz loading, tasks, error.
Mostramos 4 estados:
loading spinner,
erro,
sem tarefas,
lista de tarefas.
TaskCardRemote renderiza os campos vindos diretamente da tabela Task.*/


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    onOpenMapRoute: () -> Unit,
    onViewTaskDetails: () -> Unit,
    onBackClick: () -> Unit,
) {
    // ViewModel que fala com Supabase
    val vm: TaskRemoteViewModel = viewModel()
    val uiState by vm.ui.collectAsState()
    val context = LocalContext.current
    var expandedId by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold (
        contentWindowInsets = WindowInsets(0,0,0,0)
    ){ padding ->


        when {
            uiState.loading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Erro: ${uiState.error}",
                        color = colorScheme.error
                    )
                }
            }

            uiState.tasks.isEmpty() -> {
                Box(
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sem tarefas.")
                }
            }

            else -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(colorScheme.background)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        //contentPadding = PaddingValues(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        items(uiState.tasks, key = { it.id }) { task ->
                            val expanded = expandedId?.toInt() == task.id
                            TaskCardRemote(
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

                    //Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val json = loadJsonFromAssets(context, "route.json")
                            startRouteFromNodesJson(
                                context,
                                json,
                                includeChargingStationsAsStops = true)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp)
                            .padding(bottom = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = null
                        )
                        //Spacer(Modifier.width(8.dp))
                        Text("Start navigation")
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TaskCardRemote(
    task: TaskRemote,
    onViewTaskDetails: () -> Unit,
    onBackClick: () -> Unit,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
    ) {
    ElevatedCard(
        modifier = Modifier
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