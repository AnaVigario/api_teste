package com.example.myamover.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myamover.data.model.ClientRemoteViewModel
import com.example.myamover.data.remote.ClientRemote
import com.example.myamover.data.remote.TaskRemote
import com.example.myamover.model.TaskRemoteViewModel


/* --- Ecrã principal --- */
@Composable
fun ViewTaskDetail(
    task: TaskRemote,
    client: ClientRemote,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    onAddPhoto: () -> Unit,
    onAddSignature: () -> Unit,
    onAddNotes: () -> Unit,
    onReject: () -> Unit,
    onDeliver: () -> Unit,
    onCompleteStop: () -> Unit,
    onBack: () -> Unit
) {
    val vm: TaskRemoteViewModel = viewModel()
    val vc: ClientRemoteViewModel = viewModel()
    val uiState by vm.ui.collectAsState()
    val context = LocalContext.current
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = {
                    Column {
                        Text(task.id.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            client.address,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                }
            )
        },
        bottomBar = {
            TaskBottomBar(
                progressText = task.status,
                onReject = onReject,
                onDeliver = onDeliver,
                onCompleteStop = onCompleteStop
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Morada + estimativas
            SectionCard {
                Text(client.address, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(10.dp))
//                EstRow("Est. Arrival", ui.estArrival)
//                Spacer(Modifier.height(4.dp))
//                EstRow("Est. Departure", ui.estDeparture)
            }

            Spacer(Modifier.height(12.dp))

            // Cartão com ordem/loja/carga
            SectionCard {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Order ${task.id}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(8.dp))
//                    AssistChip(
//                        onClick = { },
//                        label = { task.timeWindow?.let { Text(it) } },
//                        leadingIcon = { Icon(Icons.Default.Schedule, null) }
//                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(client.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(6.dp))
                Text("Load", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(client.address, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(12.dp))

//            // Ações rápidas
//            SectionCard {
//                QuickRow(title = "Photos", action = "Add Photo", onClick = onAddPhoto)
//                Divider(Modifier.padding(vertical = 6.dp))
//                QuickRow(title = "Signature", action = "Add Signature", onClick = onAddSignature)
//                Divider(Modifier.padding(vertical = 6.dp))
//                QuickRow(title = "Notes to Dispatcher", action = "Add Notes", onClick = onAddNotes)
//            }

//            Spacer(Modifier.height(84.dp)) // espaço para a bottom bar
        }
    }
}

@Composable
fun SmallTopAppBar(title: @Composable () -> Unit, navigationIcon: @Composable () -> Unit) {
    TODO("Not yet implemented")
}

/* ---------- Componentes reutilizáveis ---------- */

@Composable
private fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.large
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun EstRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(8.dp))
        Text(value, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun QuickRow(title: String, action: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        TextButton(onClick = onClick) { Text(action) }
    }
}

@Composable
private fun TaskBottomBar(
    progressText: String?,
    onReject: () -> Unit,
    onDeliver: () -> Unit,
    onCompleteStop: () -> Unit
) {
    Surface(shadowElevation = 12.dp) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.Default.Close, null)
                    Spacer(Modifier.width(6.dp))
                    Text("X")
                }
                Spacer(Modifier.width(10.dp))
                OutlinedButton(
                    onClick = onDeliver,
                    modifier = Modifier.weight(1.4f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Deliver")
                }
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = onCompleteStop,
                    modifier = Modifier.weight(1.8f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.Default.Check, null)
                    Spacer(Modifier.width(6.dp))
                    Text("Complete Stop")
                }
            }
//            Box(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 8.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    progressText,
//                    style = MaterialTheme.typography.labelMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
            }
        }
    }


/* ---------- Preview rápido (podes remover) ----------
@Preview(showBackground = true, widthDp = 390)
@Composable
private fun PreviewViewTaskDetail() {
    MaterialTheme {
        ViewTaskDetail(
            ui = TaskDetailUi(
                stopTitle = "Stop 15",
                address = "2141 Granville St, Vancouver, BC V6H, Canada",
                estArrival = "12:01pm",
                estDeparture = "12:06pm",
                orderId = "ORD-12763712",
                windowTime = "8:00am - 6:00pm",
                placeName = "Cheesecake Etc",
                loadInfo = "2.0 units"
            ),
            onAddPhoto = {}, onAddSignature = {}, onAddNotes = {},
            onReject = {}, onDeliver = {}, onCompleteStop = {}, onBack = {}
        )
    }
}*/
