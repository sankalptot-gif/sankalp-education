package com.sankalp.education.data

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    val full_name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val role: String = "student"
)

object ProfileRepository {

    private val supabase
        get() = SupabaseClientProvider.client

    suspend fun getProfile(userId: String): Profile? {
        return try {
            supabase
                .from("profiles")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingleOrNull<Profile>()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getCurrentUserEmail(): String? {
        return try {
            supabase.auth.currentUserOrNull()?.email
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getCurrentUserRole(): String {
        return try {
            val currentUser = supabase.auth.currentUserOrNull()
                ?: return "student"

            val profile = getProfile(currentUser.id)

            profile?.role ?: "student"
        } catch (_: Exception) {
            "student"
        }
    }

    suspend fun logout() {
        try {
            supabase.auth.signOut()
        } catch (_: Exception) {
        }
    }
}
