package com.sankalp.education.data

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

object AuthRepository {

    private val supabase
        get() = SupabaseClientProvider.client

    suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    e.message ?: "Login failed. Email ya password check karein."
                )
            )
        }
    }

    suspend fun logout() {
        try {
            supabase.auth.signOut()
        } catch (_: Exception) {
        }
    }

    fun isLoggedIn(): Boolean {
        return supabase.auth.currentSessionOrNull() != null
    }

    fun currentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }
}
