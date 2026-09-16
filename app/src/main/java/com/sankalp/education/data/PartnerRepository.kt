package com.sankalp.education.data

import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

@Serializable
data class Partner(
    val id: String? = null,
    val user_id: String? = null,
    val partner_id: String? = null,
    val application_id: String? = null,
    val full_name: String? = null,
    val father_name: String? = null,
    val mother_name: String? = null,
    val gender: String? = null,
    val date_of_birth: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val aadhaar_number: String? = null,
    val pan_number: String? = null,
    val education_details: String? = null,
    val profile_photo_path: String? = null,
    val status: String? = null,
    val commission_percent: Double? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

@Serializable
data class PartnerInsert(
    val partner_id: String,
    val full_name: String,
    val father_name: String? = null,
    val mother_name: String? = null,
    val gender: String? = null,
    val date_of_birth: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val aadhaar_number: String? = null,
    val pan_number: String? = null,
    val education_details: String? = null,
    val status: String = "pending",
    val commission_percent: Double = 0.0
)

object PartnerRepository {

    private val supabase
        get() = SupabaseClientProvider.client

    suspend fun getPartners(): Result<List<Partner>> {
        return try {
            val partners = supabase
                .from("partners")
                .select()
                .decodeList<Partner>()

            Result.success(partners)
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    e.message ?: "Partners load nahi ho paaye."
                )
            )
        }
    }

    suspend fun addPartner(
        partner: PartnerInsert
    ): Result<Unit> {
        return try {
            supabase
                .from("partners")
                .insert(partner)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    e.message ?: "Partner save nahi ho paaya."
                )
            )
        }
    }

    suspend fun updatePartnerStatus(
        id: String,
        status: String
    ): Result<Unit> {
        return try {
            supabase
                .from("partners")
                .update(
                    mapOf("status" to status)
                ) {
                    filter {
                        eq("id", id)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    e.message ?: "Partner status update nahi ho paaya."
                )
            )
        }
    }

    suspend fun deletePartner(
        id: String
    ): Result<Unit> {
        return try {
            supabase
                .from("partners")
                .delete {
                    filter {
                        eq("id", id)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    e.message ?: "Partner delete nahi ho paaya."
                )
            )
        }
    }
}
