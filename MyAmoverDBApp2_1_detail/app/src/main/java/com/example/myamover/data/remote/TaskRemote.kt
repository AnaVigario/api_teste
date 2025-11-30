package com.example.myamover.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


//Isto é o "DTO remoto" = exatamente o que vem do Supabase.

@Serializable
data class TaskRemote(
    @SerialName("ID")
    val id: Int,

    val type: String? = null,

    @SerialName("creationDate")
    val creationDate: String? = null, // podes depois converter para Instant/LocalDateTime

    val deadline: String? = null,

    val description: String? = null,

    val status: String? = null,
//    val photo: String? = null,
//    val signature: String? = null,
//    val notes: String? = null,
//    val coordinates: String? = null,
    //val timeWindow: String? = null,


    //val coordinates: List<String>?, // ajustar mais tarde se isto for JSON/array

    @SerialName("userID")
    val userId: Int? = null,

    @SerialName("parentTaskID")
    val parentTaskId: Int? = null,

    @SerialName("serviceID")
    val serviceId: Int? = null
)
