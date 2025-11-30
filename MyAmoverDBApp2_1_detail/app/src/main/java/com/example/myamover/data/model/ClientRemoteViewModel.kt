package com.example.myamover.data.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myamover.data.remote.ClientRemote
import com.example.myamover.data.repository.ClientRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class ClientRemoteUiState(
    val loading: Boolean = false,
    val clients: List<ClientRemote> = emptyList(),
    val error: String? = null
)


class ClientRemoteViewModel(
    private val repo: ClientRemoteRepository = ClientRemoteRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(ClientRemoteUiState())
    val ui: StateFlow<ClientRemoteUiState> = _ui

    init {
        loadClients()
    }

    fun loadClients() {
        _ui.value = _ui.value.copy(loading = true, error = null)

        viewModelScope.launch {
            try {
                val all = repo.getAllClient()

                _ui.value = _ui.value.copy(
                    loading = false,
                    clients = all,
                    error = null
                )
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(
                    loading = false,
                    error = e.message ?: "Erro ao carregar clientes"
                )
            }
        }

    }

}