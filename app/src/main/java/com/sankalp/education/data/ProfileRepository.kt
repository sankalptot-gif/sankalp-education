package com.sankalp.education.data

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

    suspend fun getProfile(userId: String): Profile? {
        return try {
            SupabaseClientProvider.client
                .from("profiles")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingleOrNull<Profile>()
        } catch (e: Exception) {
            null
        }
    }
}
