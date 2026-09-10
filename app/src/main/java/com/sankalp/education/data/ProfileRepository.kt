package com.sankalp.education.data

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val full_name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val role: String = "student"
)

object ProfileRepository {

    suspend fun getMyProfile(): UserProfile? {
        return try {
            SupabaseClient.client
                .from("profiles")
                .select()
                .decodeSingle<UserProfile>()
        } catch (e: Exception) {
            null
        }
    }
}
