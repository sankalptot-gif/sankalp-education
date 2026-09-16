package com.sankalp.education.data

import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

@Serializable
data class Admission(
    val id: String? = null,
    val admission_number: String? = null,
    val partner_id: String? = null,
    val student_id: String? = null,

    val student_name: String? = null,
    val gender: String? = null,
    val father_name: String? = null,
    val mother_name: String? = null,
    val date_of_birth: String? = null,

    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,

    val course_name: String? = null,
    val fee_amount: Double? = null,
    val paid_amount: Double? = null,
    val pending_amount: Double? = null,

    val admission_date: String? = null,
    val status: String? = null,
    val remarks: String? = null,

    val commission_percent: Double? = null,
    val commission_amount: Double? = null,
    val payment_status: String? = null,

    val created_at: String? = null,
    val updated_at: String? = null
)

@Serializable
private data class AdmissionInsert(
    val admission_number: String? = null,
    val partner_id: String? = null,
    val student_id: String? = null,

    val student_name: String,
    val gender: String? = null,
    val father_name: String? = null,
    val mother_name: String? = null,
    val date_of_birth: String? = null,

    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,

    val course_name: String? = null,
    val fee_amount: Double? = null,
    val paid_amount: Double? = null,
    val pending_amount: Double? = null,

    val admission_date: String? = null,
    val status: String? = null,
    val remarks: String? = null,

    val commission_percent: Double? = null,
    val commission_amount: Double? = null,
    val payment_status: String? = null
)

object AdmissionRepository {

    private val supabase
        get() = SupabaseClientProvider.client

    suspend fun getAdmissions(): Result<List<Admission>> {
        return try {
            val admissions = supabase
                .from("admissions")
                .select()
                .decodeList<Admission>()

            Result.success(admissions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addAdmission(
        admissionNumber: String? = null,
        partnerId: String? = null,
        studentId: String? = null,

        studentName: String,
        gender: String? = null,
        fatherName: String? = null,
        motherName: String? = null,
        dateOfBirth: String? = null,

        phone: String? = null,
        email: String? = null,
        address: String? = null,

        courseName: String? = null,
        admissionDate: String? = null,

        feeAmount: Double = 0.0,
        paidAmount: Double = 0.0,

        commissionPercent: Double = 0.0,
        remarks: String? = null,

        status: String? = "pending",
        paymentStatus: String? = "pending"
    ): Result<Unit> {
        return try {
            val pendingAmount = (feeAmount - paidAmount)
                .coerceAtLeast(0.0)

            val commissionAmount =
                feeAmount * commissionPercent / 100.0

            val admission = AdmissionInsert(
                admission_number = admissionNumber,
                partner_id = partnerId,
                student_id = studentId,

                student_name = studentName,
                gender = gender,
                father_name = fatherName,
                mother_name = motherName,
                date_of_birth = dateOfBirth,

                phone = phone,
                email = email,
                address = address,

                course_name = courseName,
                fee_amount = feeAmount,
                paid_amount = paidAmount,
                pending_amount = pendingAmount,

                admission_date = admissionDate,
                status = status,
                remarks = remarks,

                commission_percent = commissionPercent,
                commission_amount = commissionAmount,
                payment_status = paymentStatus
            )

            supabase
                .from("admissions")
                .insert(admission)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAdmissionStatus(
        id: String,
        status: String,
        remarks: String? = null
    ): Result<Unit> {
        return try {
            supabase
                .from("admissions")
                .update(
                    mapOf(
                        "status" to status,
                        "remarks" to remarks
                    )
                ) {
                    filter {
                        eq("id", id)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAdmission(
        id: String
    ): Result<Unit> {
        return try {
            supabase
                .from("admissions")
                .delete {
                    filter {
                        eq("id", id)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
