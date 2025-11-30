package com.example.myamover.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myamover.data.remote.TaskRemote
import com.example.myamover.data.repository.TaskRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


//Quando abre o ecrã TasksScreen, este VM faz logo SELECT * FROM Task ao Supabase e guarda em ui.tasks.

data class TaskRemoteUiState(
    val loading: Boolean = false,
    val tasks: List<TaskRemote> = emptyList(),
    val error: String? = null
)

class TaskRemoteViewModel(
    private val repo: TaskRemoteRepository = TaskRemoteRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(TaskRemoteUiState())
    val ui: StateFlow<TaskRemoteUiState> = _ui

    init {
        loadTasks()
    }

    fun loadTasks() {
        _ui.value = _ui.value.copy(loading = true, error = null)

        viewModelScope.launch {
            try {
                // se quiseres filtrar por utilizador logado, podes mudar aqui.
                val all = repo.getAllTasks()

                _ui.value = _ui.value.copy(
                    loading = false,
                    tasks = all,
                    error = null
                )
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(
                    loading = false,
                    error = e.message ?: "Erro ao carregar tarefas"
                )
            }
        }
    }
}
