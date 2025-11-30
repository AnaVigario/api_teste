package com.example.myamover.data.repository

import com.example.myamover.data.netwok.SupabaseClientProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AuthRepository {

    private val client = SupabaseClientProvider.client

    // Login com email/password usando Supabase Auth (v3.2.5)
    suspend fun login(email: String, password: String): Result<SupabaseUserData> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. tenta autenticar
                client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                // 2. depois do signInWith, o utilizador autenticado
                // fica disponível em currentUserOrNull()
                val currentUser: UserInfo? = client.auth.currentUserOrNull()

                if (currentUser == null) {
                    return@withContext Result.failure(
                        IllegalStateException("Sessão não criada (currentUserOrNull == null)")
                    )
                }

                // 3. map para o nosso DTO externo
                Result.success(
                    currentUser.toSupabaseUserData()
                )

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // obter user atual (por ex. auto-login no arranque da app)
    suspend fun getCurrentUser(): SupabaseUserData? {
        return withContext(Dispatchers.IO) {
            client.auth.currentUserOrNull()?.toSupabaseUserData()
        }
    }

    // logout
    suspend fun logout() {
        withContext(Dispatchers.IO) {
            client.auth.signOut()
        }
    }

    // conversor interno -> externo
    private fun UserInfo.toSupabaseUserData(): SupabaseUserData {
        return SupabaseUserData(
            id = this.id,
            email = this.email ?: ""
        )
    }
}

// o DTO simples que a app realmente usa
data class SupabaseUserData(
    val id: String,
    val email: String
)