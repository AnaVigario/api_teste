package com.example.myamover.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myamover.data.netwok.SupabaseClientProvider
import com.example.myamover.data.repository.AuthRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String
)

data class LoginUiState(
    val loading: Boolean = false,
    val loggedInUser: User? = null,
    val message: String? = null
)

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    private val supabase = SupabaseClientProvider.client

    init {
        // tenta sessão guardada (auto-login)
        viewModelScope.launch {
            val current = supabase.auth.currentUserOrNull()
            if (current != null) {
                _ui.value = _ui.value.copy(
                    loggedInUser = current.toUser(),
                    message = null,
                    loading = false
                )
            }
        }
    }

    private fun UserInfo.toUser(): User? {
        return User(
            id = this.id,
            email = this.email ?: ""
        )
    }

    fun login(email: String, password: String) {
        // coloca UI em estado de loading
        _ui.value = _ui.value.copy(
            loading = true,
            message = null
        )

        viewModelScope.launch {
            try {
                // 1. tenta autenticar no Supabase (versão 3.2.5)
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                // 2. depois de fazer signInWith, a sessão fica guardada no client
                val currentUser = supabase.auth.currentUserOrNull()

                if (currentUser != null) {
                    _ui.value = _ui.value.copy(
                        loading = false,
                        loggedInUser = currentUser.toUser(),
                        message = null
                    )
                } else {
                    _ui.value = _ui.value.copy(
                        loading = false,
                        loggedInUser = null,
                        message = "Falha ao obter sessão após login (utilizador vazio)"
                    )
                }

            } catch (e: Exception) {
                _ui.value = _ui.value.copy(
                    loading = false,
                    loggedInUser = null,
                    message = "Falha no login: ${e.message ?: "credenciais inválidas?"}"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            runCatching {
                authRepository.logout()
            }.onSuccess {
                _ui.value = _ui.value.copy(
                    loggedInUser = null,
                    message = null
                )
            }.onFailure { }
        }

        // helper: converte o UserInfo (Supabase) no nosso User serializável
        fun UserInfo.toUser(): User {
            return User(
                id = this.id,
                email = this.email ?: ""
            )
        }
    }
}
